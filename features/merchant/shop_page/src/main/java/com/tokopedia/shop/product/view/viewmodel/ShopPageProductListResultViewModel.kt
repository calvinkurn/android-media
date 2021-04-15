package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.data.model.RestrictionEngineRequestParams
import com.tokopedia.shop.common.data.response.RestrictValidateRestriction
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.RestrictionEngineNplUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PRODUCT_LIST_RESULT_SOURCE
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.data.model.RestrictionEngineModel
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatus
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper.mapRestrictionEngineResponseToModel
import com.tokopedia.shop.product.view.datamodel.GetShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopEtalaseItemDataModel
import com.tokopedia.shop.product.view.datamodel.ShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopStickySortFilter
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

class ShopPageProductListResultViewModel @Inject constructor(private val userSession: UserSessionInterface,
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
                                                             private val getFollowStatusUseCase: GetFollowStatusUseCase
) : BaseViewModel(dispatcherProvider.main) {

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    val userId: String
        get() = userSession.userId

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()
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

    fun getShop(shopId: String?, shopDomain: String? = "", isRefresh: Boolean = false) {
        val id = shopId.toIntOrZero()
        if (id == 0 && shopDomain == "") return
        launchCatchError(block = {
            getShopInfoUseCase.params = GQLGetShopInfoUseCase
                    .createParams(if (id == 0) listOf() else listOf(id), shopDomain, source = SHOP_PRODUCT_LIST_RESULT_SOURCE)
            getShopInfoUseCase.isFromCacheFirst = !isRefresh
            val shopInfo = withContext(dispatcherProvider.io) { getShopInfoUseCase.executeOnBackground() }
            shopInfoResp.value = Success(shopInfo)
        }) {
            shopInfoResp.value = Fail(it)
        }
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
        toggleFavouriteShopUseCase.get().execute(ToggleFavouriteShopUseCase.createRequestParam(shopId),
                object : Subscriber<Boolean>() {
                    override fun onCompleted() {}

                    override fun onError(e: Throwable) {
                        onError(e)
                    }

                    override fun onNext(success: Boolean) {
                        onSuccess(success)
                    }
                })
    }

    fun getShopProduct(
            shopId: String,
            page: Int = 1,
            perPage: Int = 10,
            etalase: String = "",
            search: String = "",
            etalaseType: Int,
            shopProductFilterParameter: ShopProductFilterParameter,
            widgetUserAddressLocalData: LocalCacheModel
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
                                widgetUserAddressLocalData.district_id,
                                widgetUserAddressLocalData.city_id,
                                widgetUserAddressLocalData.lat,
                                widgetUserAddressLocalData.long
                        ),
                        etalaseType
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
            widgetUserAddressLocalData: LocalCacheModel
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
                getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId,
                        productFilter)
                val productListResponse = getShopProductUseCase.executeOnBackground()
                productListResponse.data.map { ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId), productFilter.etalaseMenu) }
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
            val sortResponse  = asyncCatchError(
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
                sortResponse.await()?.let{sort ->
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
            etalaseType: Int
    ): GetShopProductUiModel {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId, productFilter)
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = isHasNextPage(productFilter.page, productFilter.perPage, productListResponse.totalData)
        val totalProductData  = productListResponse.totalData
        return GetShopProductUiModel(
                isHasNextPage,
                productListResponse.data.map { ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId), productFilter.etalaseMenu, etalaseType) },
                totalProductData
        )
    }
    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    private suspend fun getSortListData(): MutableList<ShopProductSortModel> {
        val listSort = gqlGetShopSortUseCase.executeOnBackground()
        return shopProductSortMapper.convertSort(listSort).toMutableList()
    }

    fun getBottomSheetFilterData() {
        launchCatchError(coroutineContext, block = {
            val filterBottomSheetData = withContext(dispatcherProvider.io) {
                getShopFilterBottomSheetDataUseCase.params = GetShopFilterBottomSheetDataUseCase.createParams()
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
            searchKeyword: String,
            etalaseId: String,
            tempShopProductFilterParameter: ShopProductFilterParameter,
            widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(
                        shopId,
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
            searchKeyword: String,
            etalaseId: String,
            tempShopProductFilterParameter: ShopProductFilterParameter,
            widgetUserAddressLocalData: LocalCacheModel
    ): Int {
        val filter = ShopProductFilterInput(
                ShopPageConstant.START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                searchKeyword,
                etalaseId,
                tempShopProductFilterParameter.getSortId().toIntOrZero(),
                tempShopProductFilterParameter.getRating(),
                tempShopProductFilterParameter.getPmax(),
                tempShopProductFilterParameter.getPmin(),
                widgetUserAddressLocalData.district_id,
                widgetUserAddressLocalData.city_id,
                widgetUserAddressLocalData.lat,
                widgetUserAddressLocalData.long
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
}