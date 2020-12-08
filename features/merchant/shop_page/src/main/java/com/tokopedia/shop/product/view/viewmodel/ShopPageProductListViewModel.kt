package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.GetShopFilterBottomSheetDataUseCase
import com.tokopedia.shop.common.domain.GetShopFilterProductCountUseCase
import com.tokopedia.shop.common.domain.GqlGetShopSortUseCase
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCaseNew
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.common.util.ShopUtil.isFilterNotIgnored
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.product.view.datamodel.*
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.di.ShopProductGetHighlightProductQualifier
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCase
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
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
    private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
    private val getMembershipUseCase: GetMembershipUseCaseNew,
    private val userSession: UserSessionInterface,
    private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCase,
    private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
    private val getShopProductUseCase: GqlGetShopProductUseCase,
    @ShopProductGetHighlightProductQualifier
        private val getShopHighlightProductUseCase: Provider<GqlGetShopProductUseCase>,
    private val deleteShopInfoUseCase: DeleteShopInfoCacheUseCase,
    private val dispatcherProvider: CoroutineDispatchers,
    private val getShopFilterBottomSheetDataUseCase: GetShopFilterBottomSheetDataUseCase,
    private val getShopFilterProductCountUseCase: GetShopFilterProductCountUseCase,
    private val gqlGetShopSortUseCase: GqlGetShopSortUseCase,
    private val shopProductSortMapper: ShopProductSortMapper
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val SOLD_ETALASE = "sold"
        private const val DISCOUNT_ETALASE = "discount"
        private const val ORDER_BY_LAST_UPDATE = 3
        private const val ORDER_BY_MOST_SOLD = 8
        private const val NUM_VOUCHER_DISPLAY = 3
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
    val shopProductEtalaseTitleData = MutableLiveData<Result<ShopProductEtalaseTitleUiModel>>()
    val shopProductChangeProductGridSectionData = MutableLiveData<Result<Int>>()
    val productListData = MutableLiveData<Result<GetShopProductUiModel>>()
    val claimMembershipResp = MutableLiveData<Result<MembershipClaimBenefitResponse>>()
    val bottomSheetFilterLiveData = MutableLiveData<Result<DynamicFilterModel>>()
    val shopProductFilterCountLiveData = MutableLiveData<Result<Int>>()
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userDeviceId: String
        get() = userSession.deviceId
    private val listGetShopHighlightProductUseCase = mutableListOf<GqlGetShopProductUseCase?>()

    fun getBuyerShopPageProductTabData(
            shopId: String,
            etalaseList: List<ShopEtalaseItemDataModel>,
            shopEtalaseItemDataModel: ShopEtalaseItemDataModel,
            isShowNewShopHomeTab: Boolean,
            initialProductListData: GetShopProductUiModel?,
            shopProductFilterParameter: ShopProductFilterParameter
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
                    else getMerchantVoucherListData(shopId, NUM_VOUCHER_DISPLAY)
                }
                val shopProductFeaturedDataAsync = async(dispatcherProvider.io) {
                    if (isShowNewShopHomeTab) null
                    else getFeaturedProductData(shopId, userId)
                }
                val shopProductEtalaseHighlightDataAsync = async(dispatcherProvider.io) {
                    if (isShowNewShopHomeTab) null
                    else getShopProductEtalaseHighlightData(shopId, etalaseList)
                }
                val productListDataAsync = async(Dispatchers.IO) {
                    if (initialProductListData == null) {
                        getProductList(
                                getShopProductUseCase,
                                shopId,
                                START_PAGE,
                                ShopPageConstant.DEFAULT_PER_PAGE,
                                shopEtalaseItemDataModel.etalaseId,
                                "",
                                shopProductFilterParameter.getSortId().toIntOrZero(),
                                shopProductFilterParameter.getRating(),
                                shopProductFilterParameter.getPmax(),
                                shopProductFilterParameter.getPmin()
                        )
                    } else {
                        null
                    }
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
                var totalProductData = 0
                val isProductListNotEmpty = if (null != initialProductListData) {
                    totalProductData = initialProductListData.totalProductData
                    initialProductListData.listShopProductUiModel.isNotEmpty()
                } else {
                    val productListDataModel = productListDataAsync.await()
                    productListDataModel?.let {
                        productListData.postValue(Success(productListDataModel))
                        totalProductData = productListDataModel.totalProductData
                        productListDataModel.listShopProductUiModel.isNotEmpty()
                    } ?: false
                }
                if (isProductListNotEmpty) {
                    shopProductEtalaseTitleData.postValue(Success(ShopProductEtalaseTitleUiModel(
                            shopEtalaseItemDataModel.etalaseName,
                            shopEtalaseItemDataModel.etalaseBadge
                    )))
                    shopProductChangeProductGridSectionData.postValue(Success(totalProductData))
                }
            }
        },
                {
                    productListData.postValue(Fail(it))
                })
    }

    private suspend fun getShopProductEtalaseHighlightData(
            shopId: String,
            etalaseList: List<ShopEtalaseItemDataModel>
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
                            getSort(it.etalaseId)
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

    private fun getSort(etalaseId: String?): Int {
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

    private fun getMerchantVoucherListData(shopId: String, numVoucher: Int = 0): ShopMerchantVoucherUiModel? {
        return try {
            val merchantVoucherResponse = getMerchantVoucherListUseCase.createObservable(
                    GetMerchantVoucherListUseCase.createRequestParams(shopId, numVoucher)
            ).toBlocking().first()
            ShopMerchantVoucherUiModel(ShopPageProductListMapper.mapToMerchantVoucherViewModel(merchantVoucherResponse))
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun getFeaturedProductData(shopId: String, userId: String): ShopProductFeaturedUiModel? {
        try {
            getShopFeaturedProductUseCase.params = GetShopFeaturedProductUseCase.createParams(
                    shopId.toIntOrZero(),
                    userId.toIntOrZero()
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
        return shopProductSortMapper.convertSort(listSort)
    }

    private suspend fun getProductList(
            useCase: GqlGetShopProductUseCase,
            shopId: String,
            page: Int,
            perPage: Int,
            etalaseId: String,
            keyword: String,
            sortId: Int,
            rating: String = "",
            pmax: Int = 0,
            pmin: Int = 0
    ): GetShopProductUiModel {
        useCase.params = GqlGetShopProductUseCase.createParams(shopId, ShopProductFilterInput(
                page, perPage, keyword, etalaseId, sortId, rating, pmax, pmin
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

    fun getNewProductListData(
            shopId: String,
            selectedEtalaseId: String,
            shopProductFilterParameter: ShopProductFilterParameter
    ) {
        launchCatchError(block = {
            val listShopProduct = withContext(dispatcherProvider.io) {
                getProductList(
                        getShopProductUseCase,
                        shopId,
                        START_PAGE,
                        ShopPageConstant.DEFAULT_PER_PAGE,
                        selectedEtalaseId,
                        "",
                        shopProductFilterParameter.getSortId().toIntOrZero(),
                        shopProductFilterParameter.getRating(),
                        shopProductFilterParameter.getPmax(),
                        shopProductFilterParameter.getPmin()
                )
            }
            shopProductChangeProductGridSectionData.postValue(Success(listShopProduct.totalProductData))
            productListData.postValue(Success(listShopProduct))
        }) {
            productListData.postValue(Fail(it))
        }
    }

    fun getNextProductListData(
            shopId: String,
            selectedEtalaseId: String,
            page: Int,
            shopProductFilterParameter: ShopProductFilterParameter
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
                        shopProductFilterParameter.getRating(),
                        shopProductFilterParameter.getPmax(),
                        shopProductFilterParameter.getPmin()
                )
            }
            shopProductChangeProductGridSectionData.postValue(Success(listShopProduct.totalProductData))
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

    fun getNewMerchantVoucher(shopId: String) {
        launchCatchError(block = {
            val merchantVoucherData = withContext(dispatcherProvider.io) {
                getMerchantVoucherListData(shopId, NUM_VOUCHER_DISPLAY)
            }
            merchantVoucherData?.let {
                newMerchantVoucherData.postValue(Success(it))
            }
        }) {
            newMerchantVoucherData.postValue(Fail(it))
        }
    }

    fun clearCache() {
        deleteShopInfoUseCase.executeSync()
        clearMerchantVoucherCache()
        getShopEtalaseByShopUseCase.clearCache()
        clearGetShopProductUseCase()
        listGetShopHighlightProductUseCase.forEach {
            it?.clearCache()
        }
        listGetShopHighlightProductUseCase.clear()
        getShopFeaturedProductUseCase.clearCache()
    }

    fun clearMerchantVoucherCache() {
        getMerchantVoucherListUseCase.clearCache()
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

    fun setInitialProductList(initialProductListData: GetShopProductUiModel) {
        productListData.postValue(Success(initialProductListData))
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

    fun getFilterResultCount(shopId: String, tempShopProductFilterParameter: ShopProductFilterParameter) {
        launchCatchError(block = {
            val filterResultProductCount = withContext(dispatcherProvider.io) {
                getFilterResultCountData(shopId, tempShopProductFilterParameter)
            }
            shopProductFilterCountLiveData.postValue(Success(filterResultProductCount))
        }) {}
    }

    private suspend fun getFilterResultCountData(
            shopId: String,
            tempShopProductFilterParameter: ShopProductFilterParameter
    ): Int {
        val filter = ShopProductFilterInput(
                START_PAGE,
                ShopPageConstant.DEFAULT_PER_PAGE,
                "",
                "",
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