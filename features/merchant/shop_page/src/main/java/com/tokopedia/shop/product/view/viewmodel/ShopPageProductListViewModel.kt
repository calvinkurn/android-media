package com.tokopedia.shop.product.view.viewmodel

import android.content.Context
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
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.product.domain.interactor.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.product.domain.interactor.GetMembershipUseCaseNew
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil.isFilterNotIgnored
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.shop.common.util.ShopPageExceptionHandler
import com.tokopedia.shop.common.util.ShopPageMapper
import com.tokopedia.shop.home.view.viewmodel.ShopHomeViewModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.product.data.model.ShopFeaturedProductParams
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.review.shop.data.network.ErrorMessageException
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
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userDeviceId: String
        get() = userSession.deviceId
    private val listGetShopHighlightProductUseCase = mutableListOf<GqlGetShopProductUseCase>()
    private var shopSortList = mutableListOf<ShopProductSortModel>()

    fun getBuyerViewContentData(
            shopId: String,
            etalaseList: List<ShopEtalaseItemDataModel>,
            isShowNewShopHomeTab: Boolean,
            widgetUserAddressLocalData: LocalCacheModel,
            context: Context?
    ) {
        launchCatchError(coroutineContext, {
            coroutineScope {
                val membershipStampProgressDataAsync = async(dispatcherProvider.io) {
                    try {
                        getMembershipData(shopId)
                    } catch (error: Exception) {
                        null
                    }
                }
                val shopMerchantVoucherDataAsync = async(dispatcherProvider.io) {
                    if (isShowNewShopHomeTab) null
                    else getMerchantVoucherCoupon(shopId, context)
                }
                val shopProductFeaturedDataAsync = async(dispatcherProvider.io) {
                    if (isShowNewShopHomeTab) null
                    else getFeaturedProductData(shopId, userId, widgetUserAddressLocalData)
                }
                val shopProductEtalaseHighlightDataAsync = async(dispatcherProvider.io) {
                    if (isShowNewShopHomeTab) null
                    else getShopProductEtalaseHighlightData(shopId, etalaseList,widgetUserAddressLocalData)
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
                })
    }

    private suspend fun getShopProductEtalaseHighlightData(
            shopId: String,
            etalaseList: List<ShopEtalaseItemDataModel>,
            widgetUserAddressLocalData: LocalCacheModel
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
                            widgetUserAddressLocalData
                    ).listShopProductUiModel
                }
            }.awaitAll()
            val listEtalaseHighlightCarouselViewModel = mutableListOf<EtalaseHighlightCarouselUiModel>()
            listProductEtalaseHighlightResponse.forEachIndexed { index, shopProductResponse ->
                if (shopProductResponse.isNotEmpty()) {
                    listEtalaseHighlightCarouselViewModel.add(EtalaseHighlightCarouselUiModel(
                            shopProductResponse,
                            listEtalaseHighlight[index]
                    ))
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
            val response =  mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(shopId))
            val code = response.data?.resultStatus?.code
            if (code != ShopHomeViewModel.CODE_STATUS_SUCCESS) {
                val errorMessage = ErrorHandler.getErrorMessage(context, ErrorMessageException(response.data?.resultStatus?.message.toString()))
                ShopPageExceptionHandler.logExceptionToCrashlytics(
                        ShopPageExceptionHandler.ERROR_WHEN_GET_MERCHANT_VOUCHER_DATA,
                        Throwable(errorMessage)
                )
            }
            if (response.data?.isShown == true){
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
            pmin: Int = 0
    ): GetShopProductUiModel {
        useCase.params = GqlGetShopProductUseCase.createParams(shopId, ShopProductFilterInput(
                page,
                perPage,
                keyword,
                etalaseId,
                sortId,
                rating,
                pmax,
                pmin,
                widgetUserAddressLocalData.district_id,
                widgetUserAddressLocalData.city_id,
                widgetUserAddressLocalData.lat,
                widgetUserAddressLocalData.long
        ))
        val productListResponse = useCase.executeOnBackground()
        val isHasNextPage = isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        val totalProductData = productListResponse.totalData
        return GetShopProductUiModel(
                isHasNextPage,
                productListResponse.data.map { ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId), etalaseId) },
                totalProductData
        )
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
            selectedEtalaseId: String,
            shopProductFilterParameter: ShopProductFilterParameter,
            widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val listShopProduct = withContext(dispatcherProvider.io) {
                getProductList(
                        getShopProductUseCase,
                        shopId,
                        page,
                        ShopPageConstant.DEFAULT_PER_PAGE,
                        selectedEtalaseId,
                        "",
                        shopProductFilterParameter.getSortId().toIntOrZero(),
                        widgetUserAddressLocalData,
                        shopProductFilterParameter.getRating(),
                        shopProductFilterParameter.getPmax(),
                        shopProductFilterParameter.getPmin()
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

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
    }

    fun setInitialProductList(
            shopId: String,
            initialProductListData: ShopProduct.GetShopProduct
    ) {
        productListData.postValue(Success(
                GetShopProductUiModel(
                        ShopUtil.isHasNextPage(
                                START_PAGE,
                                ShopPageConstant.DEFAULT_PER_PAGE,
                                initialProductListData.totalData
                        ),
                        initialProductListData.data.map {
                            ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId), "")
                        },
                        initialProductListData.totalData
                )
        ))
    }

    fun getBottomSheetFilterData() {
        launchCatchError(coroutineContext, block = {
            val filterBottomSheetData = withContext(dispatcherProvider.io) {
                getShopFilterBottomSheetDataUseCase.params = GetShopFilterBottomSheetDataUseCase.createParams()
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
            tempShopProductFilterParameter: ShopProductFilterParameter,
            widgetUserAddressLocalData: LocalCacheModel
    ) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(shopId, tempShopProductFilterParameter, widgetUserAddressLocalData)
            }
            shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {}
    }

    private suspend fun getFilterResultCountData(
            shopId: String,
            tempShopProductFilterParameter: ShopProductFilterParameter,
            widgetUserAddressLocalData: LocalCacheModel
    ): Int {
        val filter = ShopProductFilterInput(
                START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                "",
                "",
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