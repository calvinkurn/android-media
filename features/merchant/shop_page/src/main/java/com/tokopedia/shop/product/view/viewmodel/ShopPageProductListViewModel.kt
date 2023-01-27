package com.tokopedia.shop.product.view.viewmodel

import android.content.Context
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
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.constant.ShopPageConstant.CODE_STATUS_SUCCESS
import com.tokopedia.shop.common.data.model.AffiliateAtcProductModel
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopPageMapper
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.util.ShopUtil.isFilterNotIgnored
import com.tokopedia.shop.common.util.ShopUtil.setElement
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.product.data.model.ShopFeaturedProductParams
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.product.domain.interactor.GetMembershipUseCaseNew
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Provider

class ShopPageProductListViewModel @Inject constructor(
    private val claimBenefitMembershipUseCase: ClaimBenefitMembershipUseCase,
    private val mvcSummaryUseCase: MVCSummaryUseCase,
    private val getMembershipUseCase: GetMembershipUseCaseNew,
    private val userSession: UserSessionInterface,
    private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase,
    private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    private val getShopHighlightProductUseCase: Provider<GqlGetShopProductUseCase>,
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase,
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase,
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
    private val shopProductSortMapper: ShopProductSortMapper,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val SOLD_ETALASE = "sold"
        private const val DISCOUNT_ETALASE = "discount"
        private const val ORDER_BY_LAST_UPDATE = 3
        private const val ORDER_BY_MOST_SOLD = 8
        private const val START_PAGE = 1
    }

    val userId: String
        get() = userSession.userId
    val shopSortFilterData = MutableLiveData<Result<ShopStickySortFilter>>()
    val membershipData = MutableLiveData<Result<MembershipStampProgressUiModel>>()
    val newMembershipData = MutableLiveData<Result<MembershipStampProgressUiModel>>()
    val merchantVoucherData = MutableLiveData<Result<ShopMerchantVoucherUiModel>>()
    val newMerchantVoucherData = MutableLiveData<Result<ShopMerchantVoucherUiModel>>()
    val shopProductFeaturedData = MutableLiveData<Result<ShopProductFeaturedUiModel>>()
    val shopProductEtalaseHighlightData = MutableLiveData<Result<ShopProductEtalaseHighlightUiModel>>()
    val productListData = MutableLiveData<Result<GetShopProductUiModel>>()
    val claimMembershipResp = MutableLiveData<Result<MembershipClaimBenefitResponse>>()
    val bottomSheetFilterLiveData = MutableLiveData<Result<DynamicFilterModel>>()
    val shopProductFilterCountLiveData = MutableLiveData<Result<Int>>()
    val miniCartAdd: LiveData<Result<AddToCartDataModel>>
        get() = _miniCartAdd
    private val _miniCartAdd = MutableLiveData<Result<AddToCartDataModel>>()

    val miniCartUpdate: LiveData<Result<UpdateCartV2Data>>
        get() = _miniCartUpdate
    private val _miniCartUpdate = MutableLiveData<Result<UpdateCartV2Data>>()

    val miniCartRemove: LiveData<Result<Pair<String, String>>>
        get() = _miniCartRemove
    private val _miniCartRemove = MutableLiveData<Result<Pair<String, String>>>()
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userDeviceId: String
        get() = userSession.deviceId
    private val listGetShopHighlightProductUseCase = mutableListOf<GqlGetShopProductUseCase>()
    private var shopSortList = mutableListOf<ShopProductSortModel>()
    private var miniCartData: MiniCartSimplifiedData? = null

    val updatedShopProductListQuantityData: LiveData<MutableList<Visitable<*>>>
        get() = _updatedShopProductListQuantityData
    private val _updatedShopProductListQuantityData = MutableLiveData<MutableList<Visitable<*>>>()

    val shopPageAtcTracker: LiveData<ShopPageAtcTracker>
        get() = _shopPageAtcTracker
    private val _shopPageAtcTracker = MutableLiveData<ShopPageAtcTracker>()

    val createAffiliateCookieAtcProduct: LiveData<AffiliateAtcProductModel>
        get() = _createAffiliateCookieAtcProduct
    private val _createAffiliateCookieAtcProduct = MutableLiveData<AffiliateAtcProductModel>()

    fun getBuyerViewContentData(
        shopId: String,
        etalaseList: List<ShopEtalaseItemDataModel>,
        isShopWidgetAlreadyShown: Boolean,
        widgetUserAddressLocalData: LocalCacheModel,
        context: Context?,
        isEnableDirectPurchase: Boolean
    ) {
        launchCatchError(
            coroutineContext,
            {
                coroutineScope {
                    val membershipStampProgressDataAsync = async(dispatcherProvider.io) {
                        try {
                            getMembershipData(shopId)
                        } catch (error: Exception) {
                            null
                        }
                    }
                    val shopMerchantVoucherDataAsync = async(dispatcherProvider.io) {
                        if (isShopWidgetAlreadyShown) null
                        else getMerchantVoucherCoupon(shopId, context)
                    }
                    val shopProductFeaturedDataAsync = async(dispatcherProvider.io) {
                        if (isShopWidgetAlreadyShown) null
                        else getFeaturedProductData(shopId, userId, widgetUserAddressLocalData)
                    }
                    val shopProductEtalaseHighlightDataAsync = async(dispatcherProvider.io) {
                        if (isShopWidgetAlreadyShown) null
                        else getShopProductEtalaseHighlightData(shopId, etalaseList, widgetUserAddressLocalData, isEnableDirectPurchase)
                    }
                    membershipStampProgressDataAsync.await()?.let {
                        membershipData.postValue(Success(it))
                    }
                    shopMerchantVoucherDataAsync.await()?.let {
                        merchantVoucherData.postValue(Success(it))
                    }
                    shopProductFeaturedDataAsync.await()?.let {
                        shopProductFeaturedData.postValue(Success(it))
                    }
                    shopProductEtalaseHighlightDataAsync.await()?.let {
                        shopProductEtalaseHighlightData.postValue(Success(it))
                    }
                }
            },
            {
                productListData.postValue(Fail(it))
            }
        )
    }

    private suspend fun getShopProductEtalaseHighlightData(
        shopId: String,
        etalaseList: List<ShopEtalaseItemDataModel>,
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ): ShopProductEtalaseHighlightUiModel? {
        try {
            val listEtalaseHighlight = etalaseList
                .filter { it.highlighted }
            val listProductEtalaseHighlightResponse = listEtalaseHighlight.map {
                async(dispatcherProvider.io) {
                    val getShopHighlightProductUseCase = getShopHighlightProductUseCase.get()
                    listGetShopHighlightProductUseCase.add(getShopHighlightProductUseCase)
                    getProductList(
                        getShopHighlightProductUseCase,
                        shopId,
                        START_PAGE,
                        ShopPageConstant.ETALASE_HIGHLIGHT_COUNT,
                        it.etalaseId,
                        "",
                        getSort(it.etalaseId),
                        widgetUserAddressLocalData,
                        isEnableDirectPurchase = isEnableDirectPurchase
                    ).listShopProductUiModel
                }
            }.awaitAll()
            val listEtalaseHighlightCarouselViewModel = mutableListOf<EtalaseHighlightCarouselUiModel>()
            listProductEtalaseHighlightResponse.forEachIndexed { index, shopProductResponse ->
                if (shopProductResponse.isNotEmpty()) {
                    listEtalaseHighlightCarouselViewModel.add(
                        EtalaseHighlightCarouselUiModel(
                            shopProductResponse,
                            listEtalaseHighlight[index]
                        )
                    )
                }
            }
            return ShopProductEtalaseHighlightUiModel(
                listEtalaseHighlightCarouselViewModel
            )
        } catch (error: Exception) {
            return null
        }
    }

    private fun getSort(etalaseId: String): Int {
        return when (etalaseId) {
            SOLD_ETALASE -> ORDER_BY_MOST_SOLD
            DISCOUNT_ETALASE -> ORDER_BY_LAST_UPDATE
            else -> 0
        }
    }

    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    private suspend fun getMembershipData(shopId: String): MembershipStampProgressUiModel {
        getMembershipUseCase.params = GetMembershipUseCaseNew.createRequestParams(shopId.toIntOrZero())
        val memberShipResponse = getMembershipUseCase.executeOnBackground()
        return MembershipStampProgressUiModel(ShopPageProductListMapper.mapTopMembershipViewModel(memberShipResponse))
    }

    private suspend fun getMerchantVoucherCoupon(shopId: String, context: Context?): ShopMerchantVoucherUiModel? {
        return try {
            val response = mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            val code = response.data?.resultStatus?.code
            if (code != CODE_STATUS_SUCCESS) {
                val errorMessage = ErrorHandler.getErrorMessage(context, MessageErrorException(response.data?.resultStatus?.message.toString()))
                ShopPageExceptionHandler.logExceptionToCrashlytics(
                    ShopPageExceptionHandler.ERROR_WHEN_GET_MERCHANT_VOUCHER_DATA,
                    Throwable(errorMessage)
                )
            }
            if (response.data?.isShown == true) {
                ShopMerchantVoucherUiModel(ShopPageMapper.mapToVoucherCouponUiModel(response.data, shopId))
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getFeaturedProductData(
        shopId: String,
        userId: String,
        widgetUserAddressLocalData: LocalCacheModel
    ): ShopProductFeaturedUiModel? {
        try {
            getShopFeaturedProductUseCase.params = GetShopFeaturedProductUseCase.createParams(
                ShopFeaturedProductParams(
                    shopId,
                    userId,
                    widgetUserAddressLocalData.district_id,
                    widgetUserAddressLocalData.city_id,
                    widgetUserAddressLocalData.lat,
                    widgetUserAddressLocalData.long
                )
            )
            val featuredProductResponse = getShopFeaturedProductUseCase.executeOnBackground()
            return ShopProductFeaturedUiModel(
                featuredProductResponse.map { shopFeaturedProduct ->
                    ShopPageProductListMapper.mapShopFeaturedProductToProductViewModel(
                        shopFeaturedProduct,
                        isMyShop(shopId)
                    )
                }
            )
        } catch (error: Exception) {
            return null
        }
    }

    private fun getShopEtalaseData(shopId: String): List<ShopEtalaseItemDataModel> {
        val params = GetShopEtalaseByShopUseCase.createRequestParams(shopId, !isMyShop(shopId), false, isMyShop(shopId))
        val listShopEtalaseResponse = getShopEtalaseByShopUseCase.createObservable(params).toBlocking().first()
        return ShopPageProductListMapper.mapToShopProductEtalaseListDataModel(listShopEtalaseResponse)
    }

    private suspend fun getSortListData(): MutableList<ShopProductSortModel> {
        val listSort = gqlGetShopSortUseCase.executeOnBackground()
        return shopProductSortMapper.convertSort(listSort).toMutableList()
    }

    private suspend fun getProductList(
        useCase: GqlGetShopProductUseCase,
        shopId: String,
        page: Int,
        perPage: Int,
        etalaseId: String,
        keyword: String,
        sortId: Int,
        widgetUserAddressLocalData: LocalCacheModel,
        rating: String = "",
        pmax: Int = 0,
        pmin: Int = 0,
        fcategory: Int? = null,
        extraParam: String = "",
        isEnableDirectPurchase: Boolean
    ): GetShopProductUiModel {
        useCase.params = GqlGetShopProductUseCase.createParams(
            shopId,
            ShopProductFilterInput(
                page = page,
                perPage = perPage,
                searchKeyword = keyword,
                etalaseMenu = etalaseId,
                sort = sortId,
                rating = rating,
                pmax = pmax,
                pmin = pmin,
                fcategory = fcategory,
                userDistrictId = widgetUserAddressLocalData.district_id,
                userCityId = widgetUserAddressLocalData.city_id,
                userLat = widgetUserAddressLocalData.lat,
                userLong = widgetUserAddressLocalData.long,
                extraParam = extraParam
            )
        )
        val productListResponse = useCase.executeOnBackground()
        val isHasNextPage = isHasNextPage(page, perPage, productListResponse.totalData)
        val totalProductData = productListResponse.totalData
        return GetShopProductUiModel(
            isHasNextPage,
            productListResponse.data.map {
                ShopPageProductListMapper.mapShopProductToProductViewModel(
                    it,
                    isMyShop(shopId),
                    etalaseId,
                    isEnableDirectPurchase = isEnableDirectPurchase
                )
            },
            totalProductData,
            page
        ).apply {
            updateProductCardQuantity(listShopProductUiModel.toMutableList())
        }
    }

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    fun claimMembershipBenefit(questId: Int) {
        claimBenefitMembershipUseCase.params = ClaimBenefitMembershipUseCase.createRequestParams(questId)
        launchCatchError(block = {
            claimMembershipResp.value = Success(claimBenefitMembershipUseCase.executeOnBackground())
        }) {
            claimMembershipResp.value = Fail(it)
        }
    }

    fun getProductListData(
        shopId: String,
        page: Int,
        productPerPage: Int,
        selectedEtalaseId: String,
        shopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel,
        isEnableDirectPurchase: Boolean
    ) {
        launchCatchError(block = {
            val listShopProduct = withContext(dispatcherProvider.io) {
                getProductList(
                    getShopProductUseCase,
                    shopId,
                    page,
                    productPerPage,
                    selectedEtalaseId,
                    "",
                    shopProductFilterParameter.getSortId().toIntOrZero(),
                    widgetUserAddressLocalData,
                    shopProductFilterParameter.getRating(),
                    shopProductFilterParameter.getPmax(),
                    shopProductFilterParameter.getPmin(),
                    shopProductFilterParameter.getCategory(),
                    shopProductFilterParameter.getExtraParam(),
                    isEnableDirectPurchase
                )
            }
            productListData.postValue(Success(listShopProduct))
        }) {
            productListData.postValue(Fail(it))
        }
    }

    fun getNewMembershipData(shopId: String) {
        launchCatchError(block = {
            val membershipStampProgressData = withContext(dispatcherProvider.io) { getMembershipData(shopId) }
            newMembershipData.postValue(Success(membershipStampProgressData))
        }) {
            newMembershipData.postValue(Fail(it))
        }
    }

    fun getNewMerchantVoucher(shopId: String, context: Context?) {
        launchCatchError(block = {
            val merchantVoucherData = withContext(dispatcherProvider.io) {
                getMerchantVoucherCoupon(shopId, context)
            }
            merchantVoucherData?.let {
                newMerchantVoucherData.postValue(Success(it))
            }
        }) {
            newMerchantVoucherData.postValue(Fail(it))
        }
    }

    fun clearCache() {
        getShopEtalaseByShopUseCase.clearCache()
        clearGetShopProductUseCase()
        listGetShopHighlightProductUseCase.forEach {
            it.clearCache()
        }
        listGetShopHighlightProductUseCase.clear()
        getShopFeaturedProductUseCase.clearCache()
    }

    fun getShopFilterData(shopId: String) {
        launchCatchError(block = {
            val etalaseResponse = asyncCatchError(
                dispatcherProvider.io,
                block = {
                    getShopEtalaseData(shopId)
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

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
    }

    fun setInitialProductList(
        shopId: String,
        productPerPage: Int,
        initialProductListData: ShopProduct.GetShopProduct,
        isEnableDirectPurchase: Boolean
    ) {
        productListData.postValue(
            Success(
                GetShopProductUiModel(
                    ShopUtil.isHasNextPage(
                        START_PAGE,
                        productPerPage,
                        initialProductListData.totalData
                    ),
                    initialProductListData.data.map {
                        ShopPageProductListMapper.mapShopProductToProductViewModel(
                            it,
                            isMyShop(shopId),
                            "",
                            isEnableDirectPurchase = isEnableDirectPurchase
                        )
                    },
                    initialProductListData.totalData,
                    START_PAGE // current page is 1 since its initial product list
                ).apply {
                    updateProductCardQuantity(listShopProductUiModel.toMutableList())
                }
            )
        )
    }

    fun getBottomSheetFilterData(shopId: String = "") {
        launchCatchError(coroutineContext, block = {
            val filterBottomSheetData = withContext(dispatcherProvider.io) {
                getShopFilterBottomSheetDataUseCase.params = GetShopFilterBottomSheetDataUseCase.createParams(shopId)
                getShopFilterBottomSheetDataUseCase.executeOnBackground()
            }
            filterBottomSheetData.data.let {
                it.filter = it.filter.filter { filterItem ->
                    isFilterNotIgnored(filterItem.title)
                }
            }
            bottomSheetFilterLiveData.postValue(Success(filterBottomSheetData))
        }) {
        }
    }

    fun getFilterResultCount(
        shopId: String,
        productPerPage: Int,
        tempShopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(shopId, productPerPage, tempShopProductFilterParameter, widgetUserAddressLocalData)
            }
            shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {}
    }

    private suspend fun getFilterResultCountData(
        shopId: String,
        productPerPage: Int,
        tempShopProductFilterParameter: ShopProductFilterParameter,
        widgetUserAddressLocalData: LocalCacheModel
    ): Int {
        val filter = ShopProductFilterInput(
            START_PAGE,
            productPerPage,
            "",
            "",
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

    fun handleAtcFlow(
        quantity: Int,
        shopId: String,
        componentName: String,
        shopProductUiModel: ShopProductUiModel
    ) {
        val miniCartItem = getMiniCartItem(miniCartData, shopProductUiModel.id)
        when {
            miniCartItem == null -> addItemToCart(shopProductUiModel.id, shopId, quantity, componentName, shopProductUiModel)
            quantity.isZero() -> removeItemCart(miniCartItem, componentName, shopProductUiModel)
            else -> updateItemCart(miniCartItem, quantity, componentName, shopProductUiModel)
        }
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
                atcType,
                componentName
            )
            checkShouldCreateAffiliateCookieAtcProduct(atcType, shopProductUiModel)
            _miniCartAdd.postValue(Success(it))
        }, {
            _miniCartAdd.postValue(Fail(it))
        })
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

    private fun getMiniCartItem(
        miniCartSimplifiedData: MiniCartSimplifiedData?,
        productId: String
    ): MiniCartItem.MiniCartItemProduct? {
        val items = miniCartSimplifiedData?.miniCartItems.orEmpty()
        return items.getMiniCartItemProduct(productId)
    }

    fun setMiniCartData(miniCartSimplifiedData: MiniCartSimplifiedData?) {
        miniCartData = miniCartSimplifiedData
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
        return miniCartData?.let { miniCartSimplifiedData ->
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
}
