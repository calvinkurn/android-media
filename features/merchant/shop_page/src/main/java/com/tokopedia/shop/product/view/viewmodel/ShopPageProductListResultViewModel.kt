package com.tokopedia.shop.product.view.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.SHARED_PREF_AFFILIATE_CHANNEL
import com.tokopedia.shop.common.data.model.*
import com.tokopedia.shop.common.data.response.RestrictValidateRestriction
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.di.GqlGetShopInfoForHeaderUseCaseQualifier
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.RestrictionEngineNplUseCase
import com.tokopedia.shop.common.domain.interactor.*
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PRODUCT_LIST_RESULT_SOURCE
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopsort.GqlGetShopSortUseCase
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderTickerData
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper.mapRestrictionEngineResponseToModel
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ShopPageProductListResultViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val getShopInfoUseCase: GQLGetShopInfoUseCase,
    private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
    private val shopProductSortMapper: ShopProductSortMapper,
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase,
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase,
    private val restrictionEngineNplUseCase: RestrictionEngineNplUseCase,
    private val toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>,
    private val getFollowStatusUseCase: GetFollowStatusUseCase,
    private val gqlShopPageGetDynamicTabUseCase: GqlShopPageGetDynamicTabUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val sharedPreferences: SharedPreferences,
    @GqlGetShopInfoForHeaderUseCaseQualifier
    private val gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>,
    ) : BaseViewModel(dispatcherProvider.main) {

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    val userId: String
        get() = userSession.userId

    val shopData = MutableLiveData<Result<ShopPageProductResultPageData>>()
    val productData = MutableLiveData<Result<GetShopProductUiModel>>()
    val shopSortFilterData = MutableLiveData<Result<ShopStickySortFilter>>()
    val bottomSheetFilterLiveData = MutableLiveData<Result<DynamicFilterModel>>()
    val shopProductFilterCountLiveData = MutableLiveData<Result<Int>>()

    private val _productDataEmpty = MutableLiveData<Result<List<ShopProductUiModel>>>()
    val productDataEmpty: LiveData<Result<List<ShopProductUiModel>>>
        get() = _productDataEmpty

    private val _restrictionEngineData = MutableLiveData<Result<RestrictionEngineModel>>()
    val restrictionEngineData: LiveData<Result<RestrictionEngineModel>>
        get() = _restrictionEngineData

    private var shopSortList = mutableListOf<ShopProductSortModel>()
    val miniCartData: LiveData<MiniCartSimplifiedData>
        get() = _miniCartData
    private val _miniCartData = MutableLiveData<MiniCartSimplifiedData>()
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()

    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    private val _miniCartRemove = MutableLiveData<Result<Pair<String, String>>>()

    val updatedShopProductListQuantityData: LiveData<MutableList<Visitable<*>>>
        get() = _updatedShopProductListQuantityData
    private val _updatedShopProductListQuantityData = MutableLiveData<MutableList<Visitable<*>>>()

    val shopPageAtcTracker: LiveData<ShopPageAtcTracker>
        get() = _shopPageAtcTracker
    private val _shopPageAtcTracker = MutableLiveData<ShopPageAtcTracker>()

    val createAffiliateCookieAtcProduct: LiveData<AffiliateAtcProductModel>
        get() = _createAffiliateCookieAtcProduct
    private val _createAffiliateCookieAtcProduct = MutableLiveData<AffiliateAtcProductModel>()

    val shopAffiliateChannel: LiveData<String>
        get() = _shopAffiliateChannel
    private val _shopAffiliateChannel = MutableLiveData<String>()
    val shopImagePath = MutableLiveData<String>()

    private val _shopPageShopShareData = MutableLiveData<Result<ShopInfo>>()
    val shopPageShopShareData: LiveData<Result<ShopInfo>>
        get() = _shopPageShopShareData

    fun getShop(shopId: String, shopDomain: String = "", isRefresh: Boolean = false) {
        if (shopId.toIntOrZero() == 0 && shopDomain == "") return
        launchCatchError(block = {
            val shopInfoAsync = asyncCatchError(dispatcherProvider.io, block = {
                getShopInfoResponse(
                    shopId,
                    shopDomain,
                    isRefresh
                )
            }, onError = {
                    shopData.value = Fail(it)
                    null
                })
            val shopDynamicTabDataAsync = asyncCatchError(dispatcherProvider.io, block = {
                getGqlShopPageGetDynamicTabUseCase(shopId)
            }, onError = {
                    shopData.value = Fail(it)
                    null
                })
            shopInfoAsync.await()?.let { shopInfo ->
                shopDynamicTabDataAsync.await()?.let { shopDynamicTabData ->
                    shopData.value = Success(
                        ShopPageProductResultPageData(
                            shopInfo,
                            shopDynamicTabData.shopPageGetDynamicTab
                        )
                    )
                }
            }
        }) {
            shopData.value = Fail(it)
        }
    }

    private suspend fun getGqlShopPageGetDynamicTabUseCase(shopId: String): ShopPageGetDynamicTabResponse {
        gqlShopPageGetDynamicTabUseCase.setParams(shopId.toIntOrZero(), "")
        return gqlShopPageGetDynamicTabUseCase.executeOnBackground()
    }

    private suspend fun getShopInfoResponse(
        shopId: String,
        shopDomain: String,
        isRefresh: Boolean
    ): ShopInfo {
        getShopInfoUseCase.params = GQLGetShopInfoUseCase
            .createParams(if (shopId.toIntOrZero() == 0) listOf() else listOf(shopId.toIntOrZero()), shopDomain, source = SHOP_PRODUCT_LIST_RESULT_SOURCE)
        getShopInfoUseCase.isFromCacheFirst = !isRefresh
        return getShopInfoUseCase.executeOnBackground()
    }

    fun getShopRestrictionInfo(input: RestrictionEngineRequestParams, shopId: String) {
        launchCatchError(block = {
            restrictionEngineAsync(input).await()?.run {
                val restrictionEngineResponse = mapRestrictionEngineResponseToModel(dataResponse.firstOrNull())
                getFollowStatusAsync(shopId).await()?.let {
                    restrictionEngineResponse.buttonLabel = it.followButton?.buttonLabel
                    restrictionEngineResponse.voucherIconUrl = it.followButton?.voucherIconURL
                }
                _restrictionEngineData.value = Success(restrictionEngineResponse)
            }
        }) {
            _restrictionEngineData.value = Fail(it)
        }
    }

    private suspend fun restrictionEngineAsync(input: RestrictionEngineRequestParams): Deferred<RestrictValidateRestriction?> {
        restrictionEngineNplUseCase.params = RestrictionEngineNplUseCase.createRequestParams(input)
        return asyncCatchError(dispatcherProvider.io, block = {
            restrictionEngineNplUseCase.executeOnBackground()
        }, onError = {
                _restrictionEngineData.postValue(Fail(it))
                null
            })
    }

    private suspend fun getFollowStatusAsync(shopId: String): Deferred<FollowStatus?> {
        getFollowStatusUseCase.params = GetFollowStatusUseCase.createParams(shopId)
        return asyncCatchError(dispatcherProvider.io, block = {
            getFollowStatusUseCase.executeOnBackground().followStatus
        }, onError = {
                _restrictionEngineData.postValue(Fail(it))
                null
            })
    }

    fun toggleFavorite(shopId: String, onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        toggleFavouriteShopUseCase.get().execute(
            ToggleFavouriteShopUseCase.createRequestParam(shopId),
            object : Subscriber<Boolean>() {
                override fun onCompleted() {}

                override fun onError(e: Throwable) {
                    onError(e)
                }

                override fun onNext(success: Boolean) {
                    onSuccess(success)
                }
            }
        )
    }

    fun getShopProduct(
        shopId: String,
        page: Int = 1,
        perPage: Int = 10,
        etalase: String = "",
        search: String = "",
        etalaseType: Int,
        shopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ) {
        launchCatchError(block = {
            val getProductResp = withContext(dispatcherProvider.io) {
                getShopProductData(
                    shopId,
                    ShopProductFilterInput(
                        page,
                        perPage,
                        search,
                        etalase,
                        shopProductFilterParameter.getSortId().toIntOrZero(),
                        shopProductFilterParameter.getRating(),
                        shopProductFilterParameter.getPmax(),
                        shopProductFilterParameter.getPmin(),
                        shopProductFilterParameter.getCategory(),
                        widgetUserAddressLocalData.district_id,
                        widgetUserAddressLocalData.city_id,
                        widgetUserAddressLocalData.lat,
                        widgetUserAddressLocalData.long,
                        shopProductFilterParameter.getExtraParam()
                    ),
                    etalaseType,
                    isEnableDirectPurchase
                )
            }
            productData.postValue(Success(getProductResp))
        }) {
            productData.postValue(Fail(it))
        }
    }

    fun getShopProductEmptyState(
        shopId: String,
        page: Int = 1,
        perPage: Int = 10,
        sortId: Int = 0,
        etalase: String = "",
        search: String = "",
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ) {
        launchCatchError(block = {
            val getProductResp = withContext(dispatcherProvider.io) {
                val productFilter = ShopProductFilterInput().apply {
                    this.page = page
                    this.perPage = perPage
                    searchKeyword = search
                    etalaseMenu = etalase
                    sort = sortId
                    userDistrictId = widgetUserAddressLocalData.district_id
                    userCityId = widgetUserAddressLocalData.city_id
                    userLat = widgetUserAddressLocalData.lat
                    userLong = widgetUserAddressLocalData.long
                }
                getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
                    shopId,
                    productFilter
                )
                val productListResponse = getShopProductUseCase.executeOnBackground()
                productListResponse.data.map {
                    ShopPageProductListMapper.mapShopProductToProductViewModel(
                        it,
                        isMyShop(shopId),
                        productFilter.etalaseMenu,
                        isEnableDirectPurchase = isEnableDirectPurchase
                    )
                }
            }
            _productDataEmpty.postValue(Success(getProductResp))
        }) {
            _productDataEmpty.postValue(Fail(it))
        }
    }

    fun clearCache() {
        getShopEtalaseByShopUseCase.clearCache()
        clearGetShopProductUseCase()
    }

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
    }

    fun getShopFilterData(
        shopInfo: ShopInfo,
        isOwner: Boolean,
        isForceRefresh: Boolean = false
    ) {
        launchCatchError(coroutineContext, block = {
            val etalaseResponse = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getShopEtalaseData(shopInfo.shopCore.shopID, isOwner, isForceRefresh)
                },
                onError = {
                    shopSortFilterData.postValue(Fail(it))
                    null
                }
            )
            val sortResponse = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getSortListData()
                },
                onError = {
                    shopSortFilterData.postValue(Fail(it))
                    null
                }
            )
            etalaseResponse.await()?.let { etalase ->
                sortResponse.await()?.let { sort ->
                    shopSortList = sort
                    shopSortFilterData.postValue(Success(ShopStickySortFilter(etalase, sort)))
                }
            }
        }) {
            shopSortFilterData.postValue(Fail(it))
        }
    }

    private fun getShopEtalaseData(shopId: String, isOwner: Boolean, isNeedToReloadData: Boolean): List<ShopEtalaseItemDataModel> {
        val params: RequestParams = if (isOwner) {
            GetShopEtalaseByShopUseCase.createRequestParams(
                shopId,
                GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_NO_COUNT_VALUE,
                GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_SHOWCASE_GROUP_VALUE,
                GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.IS_OWNER_VALUE
            )
        } else {
            GetShopEtalaseByShopUseCase.createRequestParams(
                shopId,
                GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_NO_COUNT_VALUE,
                GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_SHOWCASE_GROUP_VALUE,
                GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.IS_OWNER_VALUE
            )
        }
        getShopEtalaseByShopUseCase.isFromCacheFirst = !isNeedToReloadData
        val listShopEtalaseResponse = getShopEtalaseByShopUseCase.createObservable(params).toBlocking().first()
        return ShopPageProductListMapper.mapToShopProductEtalaseListDataModel(listShopEtalaseResponse)
    }

    private suspend fun getShopProductData(
        shopId: String,
        productFilter: ShopProductFilterInput,
        etalaseType: Int,
        isEnableDirectPurchase: Boolean
    ): GetShopProductUiModel {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId, productFilter)
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = isHasNextPage(productFilter.page, productFilter.perPage, productListResponse.totalData)
        val totalProductData = productListResponse.totalData
        return GetShopProductUiModel(
            isHasNextPage,
            productListResponse.data.map {
                ShopPageProductListMapper.mapShopProductToProductViewModel(
                    it,
                    isMyShop(shopId),
                    productFilter.etalaseMenu,
                    etalaseType,
                    isEnableDirectPurchase
                )
            },
            totalProductData,
            productFilter.page,
            GetShopProductSuggestionUiModel(
                productListResponse.suggestion.text,
                productListResponse.suggestion.query,
                productListResponse.suggestion.responseCode,
                productListResponse.suggestion.keywordProcess
            )
        ).apply {
            updateProductCardQuantity(listShopProductUiModel.toMutableList())
        }
    }

    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    private suspend fun getSortListData(): MutableList<ShopProductSortModel> {
        val listSort = gqlGetShopSortUseCase.executeOnBackground()
        return shopProductSortMapper.convertSort(listSort).toMutableList()
    }

    fun getBottomSheetFilterData(shopId: String = "") {
        launchCatchError(coroutineContext, block = {
            val filterBottomSheetData = withContext(dispatcherProvider.io) {
                getShopFilterBottomSheetDataUseCase.params = GetShopFilterBottomSheetDataUseCase.createParams(shopId)
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            }
            filterBottomSheetData.data.let {
                it.filter = it.filter.filter { filterItem ->
                    ShopUtil.isFilterNotIgnored(filterItem.title)
                }
            }
            bottomSheetFilterLiveData.postValue(Success(filterBottomSheetData))
        }) {
        }
    }

    fun getFilterResultCount(
        shopId: String,
        productPerPage: Int,
        searchKeyword: String,
        etalaseId: String,
        tempShopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(
                    shopId,
                    productPerPage,
                    searchKeyword,
                    etalaseId,
                    tempShopProductFilterParameter,
                    widgetUserAddressLocalData
                )
            }
            shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {
            shopProductFilterCountLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getFilterResultCountData(
        shopId: String,
        productPerPage: Int,
        searchKeyword: String,
        etalaseId: String,
        tempShopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel
    ): Int {
        val filter = ShopProductFilterInput(
            ShopPageConstant.START_PAGE,
            productPerPage,
            searchKeyword,
            etalaseId,
            tempShopProductFilterParameter.getSortId().toIntOrZero(),
            tempShopProductFilterParameter.getRating(),
            tempShopProductFilterParameter.getPmax(),
            tempShopProductFilterParameter.getPmin(),
            tempShopProductFilterParameter.getCategory(),
            widgetUserAddressLocalData.district_id,
            widgetUserAddressLocalData.city_id,
            widgetUserAddressLocalData.lat,
            widgetUserAddressLocalData.long,
            tempShopProductFilterParameter.getExtraParam()
        )
        getShopFilterProductCountUseCase.params = GetShopFilterProductCountUseCase.createParams(
            shopId,
            filter
        )
        return getShopFilterProductCountUseCase.executeOnBackground()
    }

    fun getSortNameById(sortId: String): String {
        return shopSortList.firstOrNull {
            it.value == sortId
        }?.name.orEmpty()
    }

    fun updateMiniCartData(miniCartSimplifiedData: MiniCartSimplifiedData) {
        _miniCartData.postValue(miniCartSimplifiedData)
    }

    fun handleAtcFlow(
        quantity: Int,
        shopId: String,
        componentName: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        val miniCartItem = getMiniCartItem(miniCartData.value, shopProductUiModel.id)
        when {
            miniCartItem == null -> addItemToCart(shopProductUiModel.id, shopId, quantity, componentName, shopProductUiModel)
            quantity.isZero() -> removeItemCart(miniCartItem, componentName, shopProductUiModel)
            else -> updateItemCart(miniCartItem, quantity, componentName, shopProductUiModel)
        }
    }

    private fun getMiniCartItem(
        miniCartSimplifiedData: MiniCartSimplifiedData?,
        productId: String
    ): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    private fun addItemToCart(
        productId: String,
        shopId: String,
        quantity: Int,
        componentName: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        val addToCartRequestParams =
            AddToCartUseCase.getMinimumParams(
                productId = productId,
                shopId = shopId,
                quantity = quantity,
                atcExternalSource = AtcFromExternalSource.ATC_FROM_SHOP
            )
        addToCartUseCase.setParams(addToCartRequestParams)
        addToCartUseCase.execute({
            val atcType = ShopPageAtcTracker.AtcType.ADD
            trackAddToCart(
                it.data.cartId,
                it.data.productId.toString(),
                shopProductUiModel.name,
                shopProductUiModel.displayedPrice,
                shopProductUiModel.isVariant,
                it.data.quantity,
                ShopPageAtcTracker.AtcType.ADD,
                componentName
            )
            checkShouldCreateAffiliateCookieAtcProduct(atcType, shopProductUiModel)
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
    }

    private fun checkShouldCreateAffiliateCookieAtcProduct(
        atcType: ShopPageAtcTracker.AtcType,
        shopProductUiModel: ShopProductUiModel
    ) {
        when (atcType) {
            ShopPageAtcTracker.AtcType.ADD, ShopPageAtcTracker.AtcType.UPDATE_ADD -> {
                _createAffiliateCookieAtcProduct.postValue(
                    AffiliateAtcProductModel(
                        shopProductUiModel.id,
                        shopProductUiModel.isVariant,
                        shopProductUiModel.stock.toInt()
                    )
                )
            }
            else -> {}
        }
    }

    private fun updateItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        quantity: Int,
        componentName: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        val existingQuantity = miniCartItem.quantity
        miniCartItem.quantity = quantity
        val cartId = miniCartItem.cartId
        val updateCartRequest = UpdateCartRequest(
            cartId = cartId,
            quantity = miniCartItem.quantity,
            notes = miniCartItem.notes
        )
        updateCartUseCase.setParams(
            updateCartRequestList = listOf(updateCartRequest),
            source = UpdateCartUseCase.VALUE_SOURCE_UPDATE_QTY_NOTES
        )
        updateCartUseCase.execute({
            val atcType = if (quantity < existingQuantity) {
                ShopPageAtcTracker.AtcType.UPDATE_REMOVE
            } else {
                ShopPageAtcTracker.AtcType.UPDATE_ADD
            }
            trackAddToCart(
                miniCartItem.cartId,
                miniCartItem.productId,
                shopProductUiModel.name,
                shopProductUiModel.displayedPrice,
                shopProductUiModel.isVariant,
                miniCartItem.quantity,
                atcType,
                componentName
            )
            checkShouldCreateAffiliateCookieAtcProduct(atcType, shopProductUiModel)
            _miniCartUpdate.value = Success(it)
        }, {
            _miniCartUpdate.postValue(Fail(it))
        })
    }

    private fun removeItemCart(
        miniCartItem: MiniCartItem.MiniCartItemProduct,
        componentName: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        deleteCartUseCase.setParams(
            cartIdList = listOf(miniCartItem.cartId)
        )
        deleteCartUseCase.execute({
            val productId = miniCartItem.productId
            val data = Pair(productId, it.data.message.joinToString(separator = ", "))
            trackAddToCart(
                miniCartItem.cartId,
                miniCartItem.productId,
                shopProductUiModel.name,
                shopProductUiModel.displayedPrice,
                shopProductUiModel.isVariant,
                miniCartItem.quantity,
                ShopPageAtcTracker.AtcType.REMOVE,
                componentName
            )
            _miniCartRemove.postValue(Success(data))
        }, {
            _miniCartRemove.postValue(Fail(it))
        })
    }

    fun getShopProductDataWithUpdatedQuantity(listProductTabWidget: MutableList<Visitable<*>>) {
        updateProductCardQuantity(listProductTabWidget)
        _updatedShopProductListQuantityData.postValue(listProductTabWidget)
    }

    private fun updateProductCardQuantity(listProductTabWidget: MutableList<Visitable<*>>) {
        listProductTabWidget.forEachIndexed { index, productTabWidget ->
            when (productTabWidget) {
                is ShopProductUiModel -> {
                    updateShopProductUiModelQuantity(productTabWidget).let {
                        listProductTabWidget.setElement(index, it)
                    }
                }
            }
        }
    }

    private fun updateShopProductUiModelQuantity(productModel: ShopProductUiModel): ShopProductUiModel {
        val matchedMiniCartItem = getMatchedMiniCartItem(productModel)
        if (matchedMiniCartItem.isNotEmpty()) {
            val cartQuantity = matchedMiniCartItem.sumOf {
                it.quantity.orZero()
            }
            if (cartQuantity != productModel.productInCart) {
                productModel.productInCart = cartQuantity
                productModel.isNewData = true
            } else {
                productModel.isNewData = false
            }
        } else {
            if (!productModel.productInCart.isZero()) {
                productModel.productInCart = 0
                productModel.isNewData = true
            } else {
                productModel.productInCart = 0
                productModel.isNewData = false
            }
        }
        return productModel
    }

    private fun getMatchedMiniCartItem(
        shopProductUiModel: ShopProductUiModel
    ): List<MiniCartItem.MiniCartItemProduct> {
        return miniCartData.value?.let { miniCartSimplifiedData ->
            val isVariant = shopProductUiModel.isVariant
            val listMatchedMiniCartItemProduct = if (isVariant) {
                miniCartSimplifiedData.miniCartItems.values.filterIsInstance<MiniCartItem.MiniCartItemProduct>()
                    .filter { it.productParentId == shopProductUiModel.parentId }
            } else {
                val childProductId = shopProductUiModel.id
                miniCartSimplifiedData.miniCartItems.getMiniCartItemProduct(childProductId)?.let {
                    listOf(it)
                }.orEmpty()
            }
            listMatchedMiniCartItemProduct.filter { !it.isError }
        }.orEmpty()
    }

    private fun trackAddToCart(
        cartId: String,
        productId: String,
        productName: String,
        productPrice: String,
        isVariant: Boolean,
        quantity: Int,
        atcType: ShopPageAtcTracker.AtcType,
        componentName: String
    ) {
        val mvcLockedToProductAddToCartTracker = ShopPageAtcTracker(
            cartId,
            productId,
            productName,
            productPrice,
            isVariant,
            quantity,
            atcType,
            componentName
        )
        _shopPageAtcTracker.postValue(mvcLockedToProductAddToCartTracker)
    }

    fun initAffiliateCookie(
        affiliateCookieHelper: AffiliateCookieHelper,
        shopId: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            affiliateCookieHelper.initCookie(
                "",
                "",
                AffiliatePageDetail(shopId, AffiliateSdkPageSource.Shop(shopId))
            )
        }) {
        }
    }

    fun createAffiliateCookieShopAtcProduct(
        affiliateCookieHelper: AffiliateCookieHelper,
        affiliateChannel: String,
        productId: String,
        isVariant: Boolean,
        stockQty: Int,
        shopId: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val affiliateSdkDirectAtcSource = AffiliateSdkPageSource.DirectATC(
                AffiliateAtcSource.SHOP_PAGE,
                shopId,
                AffiliateSdkProductInfo("", isVariant, stockQty)
            )
            affiliateCookieHelper.initCookie(
                "",
                affiliateChannel,
                AffiliatePageDetail(productId, affiliateSdkDirectAtcSource)
            )
        }) {
        }
    }

    fun getShopAffiliateChannel() {
        launchCatchError(dispatcherProvider.io, block = {
            val shopAffiliateChannel = sharedPreferences.getString(SHARED_PREF_AFFILIATE_CHANNEL, "")
            _shopAffiliateChannel.postValue(shopAffiliateChannel)
        }) {}
    }

    fun getShopShareData(shopId: String, shopDomain: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val shopInfoData = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getShopInfoHeader(
                        shopId.toIntOrZero(),
                        shopDomain
                    )
                },
                onError = {
                    null
                }
            )
            shopInfoData.await()?.let { shopInfo ->
                _shopPageShopShareData.postValue(Success(shopInfo))
            }
        }) {}
    }

    private suspend fun getShopInfoHeader(shopId: Int, shopDomain: String): ShopInfo {
        gqlGetShopInfoForHeaderUseCase.get().params = GQLGetShopInfoUseCase.createParams(
            if (shopId == 0) listOf() else listOf(shopId),
            shopDomain,
            source = GQLGetShopInfoUseCase.SHOP_PAGE_SOURCE,
            fields = listOf(
                GQLGetShopInfoUseCase.FIELD_CORE,
                GQLGetShopInfoUseCase.FIELD_ASSETS,
                GQLGetShopInfoUseCase.FIELD_LAST_ACTIVE,
                GQLGetShopInfoUseCase.FIELD_LOCATION,
                GQLGetShopInfoUseCase.FIELD_ALLOW_MANAGE,
                GQLGetShopInfoUseCase.FIELD_IS_OWNER,
                GQLGetShopInfoUseCase.FIELD_STATUS,
                GQLGetShopInfoUseCase.FIELD_IS_OPEN,
                GQLGetShopInfoUseCase.FIELD_CLOSED_INFO,
                GQLGetShopInfoUseCase.FIELD_CREATE_INFO,
                GQLGetShopInfoUseCase.FIELD_SHOP_SNIPPET,
                GQLGetShopInfoUseCase.FIELD_BRANCH_LINK
            )
        )
        return gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
    }

}
