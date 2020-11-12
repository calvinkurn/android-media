package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PRODUCT_LIST_RESULT_SOURCE
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.coroutines.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.GetShopProductUiModel
import com.tokopedia.shop.product.view.datamodel.ShopEtalaseItemDataModel
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.product.view.datamodel.ShopStickySortFilter
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopPageProductListResultViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                             private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                                             private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                             private val getShopProductUseCase: GqlGetShopProductUseCase,
                                                             private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
                                                             private val shopProductSortMapper: ShopProductSortMapper,
                                                             private val dispatcherProvider: CoroutineDispatchers,
                                                             private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase,
                                                             private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase
) : BaseViewModel(dispatcherProvider.main) {

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()
    val productData = MutableLiveData<Result<GetShopProductUiModel>>()
    val shopSortFilterData = MutableLiveData<Result<ShopStickySortFilter>>()
    val bottomSheetFilterLiveData = MutableLiveData<Result<DynamicFilterModel>>()
    val shopProductFilterCountLiveData = MutableLiveData<Result<Int>>()
    private val _productDataEmpty = MutableLiveData<Result<List<ShopProductViewModel>>>()
    val productDataEmpty: LiveData<Result<List<ShopProductViewModel>>>
        get() = _productDataEmpty

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false) {
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
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

    fun getShopProduct(
            shopId: String,
            page: Int = 1,
            perPage: Int = 10,
            etalase: String = "",
            search: String = "",
            isForceRefresh: Boolean = false,
            etalaseType: Int,
            shopProductFilterParameter: ShopProductFilterParameter
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
                                shopProductFilterParameter.getPmin()
                        ),
                        isForceRefresh,
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
            isForceRefresh: Boolean = true
    ) {
        launchCatchError(block = {
            val getProductResp = withContext(Dispatchers.IO) {
                val productFilter = ShopProductFilterInput(page, perPage, search, etalase, sortId)
                getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId,
                        productFilter)
                getShopProductUseCase.isFromCacheFirst = !isForceRefresh
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
                    shopSortFilterData.postValue(Success(ShopStickySortFilter(etalase, sort)))
                }
            }
        }) {
            shopSortFilterData.postValue(Fail(it))
        }
    }

    private fun getShopEtalaseData(shopId: String, isOwner: Boolean, isNeedToReloadData: Boolean = false): List<ShopEtalaseItemDataModel> {
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
            isForceRefresh: Boolean = false,
            etalaseType: Int
    ): GetShopProductUiModel {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId, productFilter)
        getShopProductUseCase.isFromCacheFirst = !isForceRefresh
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
        return shopProductSortMapper.convertSort(listSort)
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

    fun getFilterResultCount(shopId: String, searchKeyword: String, etalaseId: String, tempShopProductFilterParameter: ShopProductFilterParameter) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(shopId, searchKeyword, etalaseId, tempShopProductFilterParameter)
            }
            shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {}
    }

    private suspend fun getFilterResultCountData(
            shopId: String,
            searchKeyword: String,
            etalaseId: String,
            tempShopProductFilterParameter: ShopProductFilterParameter
    ): Int {
        val filter = ShopProductFilterInput(
                ShopPageConstant.START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                searchKeyword,
                etalaseId,
                tempShopProductFilterParameter.getSortId().toIntOrZero(),
                tempShopProductFilterParameter.getRating(),
                tempShopProductFilterParameter.getPmax(),
                tempShopProductFilterParameter.getPmin()
        )
        getShopFilterProductCountUseCase.params = GetShopFilterProductCountUseCase.createParams(
                shopId,
                filter
        )
        return getShopFilterProductCountUseCase.executeOnBackground()
    }

    fun getSortNameById(sortId: String): String {
        return (shopSortFilterData.value as? Success)?.data?.sortList?.firstOrNull {
            it.value == sortId
        }?.name.orEmpty()
    }
}