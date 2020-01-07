package com.tokopedia.shop.newproduct.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.merchantvoucher.common.gql.domain.usecase.GetMerchantVoucherListUseCase
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.interactor.DeleteShopInfoCacheUseCase
import com.tokopedia.shop.common.graphql.data.membershipclaimbenefit.MembershipClaimBenefitResponse
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.ClaimBenefitMembershipUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetMembershipUseCaseNew
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.newproduct.view.datamodel.*
import com.tokopedia.shop.newproduct.view.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GetShopFeaturedProductUseCaseNew
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Provider

class ShopPageProductListViewModel @Inject constructor(
        private val claimBenefitMembershipUseCase: ClaimBenefitMembershipUseCase,
        private val getMerchantVoucherListUseCase: GetMerchantVoucherListUseCase,
        private val getMembershipUseCase: GetMembershipUseCaseNew,
        private val userSession: UserSessionInterface,
        private val getShopFeaturedProductUseCase: GetShopFeaturedProductUseCaseNew,
        private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val getShopProductUseCase: Provider<GqlGetShopProductUseCase>,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val deleteShopInfoUseCase: DeleteShopInfoCacheUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private const val SOLD_ETALASE = "sold"
        private const val DISCOUNT_ETALASE = "discount"
        private const val ORDER_BY_LAST_UPDATE = 3
        private const val ORDER_BY_MOST_SOLD = 8
        private const val NUM_VOUCHER_DISPLAY = 3

    }

    val userId: String
        get() = userSession.userId
    val etalaseListData = MutableLiveData<Result<ShopProductEtalaseListViewModel>>()
    val membershipData = MutableLiveData<Result<MembershipStampProgressViewModel>>()
    val newMembershipData = MutableLiveData<Result<MembershipStampProgressViewModel>>()
    val merchantVoucherData = MutableLiveData<Result<ShopMerchantVoucherViewModel>>()
    val newMerchantVoucherData = MutableLiveData<Result<ShopMerchantVoucherViewModel>>()
    val shopProductFeaturedData = MutableLiveData<Result<ShopProductFeaturedViewModel>>()
    val shopProductEtalaseHighlightData = MutableLiveData<Result<ShopProductEtalaseHighlightViewModel>>()
    val shopProductEtalaseTitleData = MutableLiveData<Result<ShopProductEtalaseTitleViewModel>>()
    val productListData = MutableLiveData<Result<Pair<Boolean, List<ShopProductViewModel>>>>()
    val claimMembershipResp = MutableLiveData<Result<MembershipClaimBenefitResponse>>()
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userDeviceId: String
        get() = userSession.deviceId

    fun getBuyerShopPageProductTabData(shopId: String, shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel) {
        launchCatchError(coroutineContext, {
            coroutineScope {
                val membershipStampProgressDataAsync = async(Dispatchers.IO) {
                    try {
                        getMembershipData(shopId)
                    } catch (error: Exception) {
                        null
                    }
                }
                val shopMerchantVoucherDataAsync = async(Dispatchers.IO) {
                    try {
                        getMerchantVoucherListData(shopId, NUM_VOUCHER_DISPLAY)
                    } catch (error: Exception) {
                        null
                    }
                }
                val shopProductFeaturedDataAsync = async(Dispatchers.IO) { getFeaturedProductData(shopId, userId) }
                val shopProductEtalaseHighlightDataAsync = async(Dispatchers.IO) { getShopProductEtalaseHighlightData(shopId, shopProductEtalaseListViewModel) }
                val productListDataAsync = async(Dispatchers.IO) {
                    getProductList(shopId, ShopProductFilterInput().apply {
                        this.page = 1
                        this.perPage = ShopPageConstant.DEFAULT_PER_PAGE
                        this.etalaseMenu = shopProductEtalaseListViewModel.selectedEtalaseId
                        this.searchKeyword = ""
                        this.sort = 0
                    })
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
                val productListDataModel = productListDataAsync.await()
                if (productListDataModel.second.isNotEmpty()) {
                    shopProductEtalaseTitleData.postValue(Success(ShopProductEtalaseTitleViewModel(
                            shopProductEtalaseListViewModel.selectedEtalaseName,
                            shopProductEtalaseListViewModel.selectedEtalaseBadge
                    )))
                }
                productListData.postValue(Success(productListDataModel))
            }
        }, {
            productListData.postValue(Fail(it))
        })
    }


    fun getSellerShopPageProductTabData(shopId: String, shopProductEtalaseListViewModel: ShopProductEtalaseListViewModel) {
        launchCatchError(coroutineContext, {
            coroutineScope {
                val productListDataAsync = async(Dispatchers.IO) {
                    getProductList(shopId, ShopProductFilterInput().apply {
                        this.page = 1
                        this.perPage = ShopPageConstant.DEFAULT_PER_PAGE
                        this.etalaseMenu = shopProductEtalaseListViewModel.selectedEtalaseId
                        this.searchKeyword = ""
                        this.sort = 0
                    })
                }
                val productListDataModel = productListDataAsync.await()
                if (productListDataModel.second.isNotEmpty()) {
                    shopProductEtalaseTitleData.postValue(Success(ShopProductEtalaseTitleViewModel(
                            shopProductEtalaseListViewModel.selectedEtalaseName,
                            shopProductEtalaseListViewModel.selectedEtalaseBadge
                    )))
                }
                productListData.postValue(Success(productListDataModel))
            }
        }, {
            productListData.postValue(Fail(it))
        })
    }

    private suspend fun getShopProductEtalaseHighlightData(
            shopId: String,
            etalaseListViewModel: ShopProductEtalaseListViewModel
    ): ShopProductEtalaseHighlightViewModel? {
        try {
            val listEtalaseHighlight = etalaseListViewModel.etalaseModelList
                    .filterIsInstance(ShopProductEtalaseChipItemViewModel::class.java)
                    .filter { it.highlighted }
            val listProductEtalaseHighlightResponse = listEtalaseHighlight.map {
                val productFilter = ShopProductFilterInput().apply {
                    this.page = 1
                    this.perPage = ShopPageConstant.ETALASE_HIGHLIGHT_COUNT
                    this.etalaseMenu = it.etalaseId
                    this.searchKeyword = ""
                    this.sort = getSort(it.etalaseId)
                }
                async(Dispatchers.IO) {
                    getProductList(shopId, productFilter).second
                }
            }.awaitAll()
            val listEtalaseHighlightCarouselViewModel = mutableListOf<EtalaseHighlightCarouselViewModel>()
            listProductEtalaseHighlightResponse.forEachIndexed { index, shopProductResponse ->
                if (shopProductResponse.isNotEmpty()) {
                    listEtalaseHighlightCarouselViewModel.add(EtalaseHighlightCarouselViewModel(
                            shopProductResponse,
                            listEtalaseHighlight[index]
                    ))
                }
            }
            return ShopProductEtalaseHighlightViewModel(
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

    private suspend fun getMembershipData(shopId: String): MembershipStampProgressViewModel {
        getMembershipUseCase.params = GetMembershipUseCaseNew.createRequestParams(shopId.toIntOrZero())
        val memberShipResponse = getMembershipUseCase.executeOnBackground()
        return MembershipStampProgressViewModel(ShopPageProductListMapper.mapTopMembershipViewModel(memberShipResponse))
    }

    private fun getMerchantVoucherListData(shopId: String, numVoucher: Int = 0): ShopMerchantVoucherViewModel {
        val merchantVoucherResponse = getMerchantVoucherListUseCase.createObservable(
                GetMerchantVoucherListUseCase.createRequestParams(shopId, numVoucher)
        ).toBlocking().first()
        return ShopMerchantVoucherViewModel(ShopPageProductListMapper.mapToMerchantVoucherViewModel(merchantVoucherResponse))
    }

    private suspend fun getFeaturedProductData(shopId: String, userId: String): ShopProductFeaturedViewModel? {
        try {
            getShopFeaturedProductUseCase.params = GetShopFeaturedProductUseCaseNew.createParams(
                    shopId.toIntOrZero(),
                    userId.toIntOrZero()
            )
            val featuredProductResponse = getShopFeaturedProductUseCase.executeOnBackground()
            return ShopProductFeaturedViewModel(
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

    private fun getShopEtalaseData(shopId: String, selectedEtalaseId: String): ShopProductEtalaseListViewModel {
        val params = GetShopEtalaseByShopUseCase.createRequestParams(shopId, true, false, isMyShop(shopId))
        val listShopEtalaseResponse = getShopEtalaseByShopUseCase.createObservable(params).toBlocking().first()
        val firstShopEtalase = listShopEtalaseResponse.first()
        val fixSelectedEtalaseId = if (selectedEtalaseId.isEmpty()) {
            if (firstShopEtalase.type == ETALASE_DEFAULT) firstShopEtalase.alias else firstShopEtalase.id
        } else {
            selectedEtalaseId
        }
        val selectedEtalaseName = firstShopEtalase.name
        val selectedEtalaseBadge = firstShopEtalase.badge
        return ShopPageProductListMapper.mapToShopProductEtalaseListDataModel(
                listShopEtalaseResponse,
                fixSelectedEtalaseId,
                selectedEtalaseName,
                selectedEtalaseBadge,
                isMyShop(shopId)
        )
    }

    private suspend fun getProductList(
            shopId: String,
            productFilter: ShopProductFilterInput
    ): Pair<Boolean, List<ShopProductViewModel>> {
        val getShopProductUseCase = getShopProductUseCase.get()
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId, productFilter)
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = isHasNextPage(productFilter.page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        return Pair(
                isHasNextPage,
                productListResponse.data.map { ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId)) }
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
            selectedEtalaseId: String
    ) {
        launchCatchError(block = {
            val listShopProduct = withContext(Dispatchers.IO) {
                getProductList(shopId, ShopProductFilterInput().apply {
                    this.page = 1
                    this.perPage = ShopPageConstant.DEFAULT_PER_PAGE
                    this.etalaseMenu = selectedEtalaseId
                    this.searchKeyword = ""
                    this.sort = 0
                })
            }
            productListData.postValue(Success(listShopProduct))
        }) {
            productListData.postValue(Fail(it))
        }
    }

    fun getNextProductListData(
            shopId: String,
            selectedEtalaseId: String,
            page: Int
    ) {
        launchCatchError(block = {
            val listShopProduct = withContext(Dispatchers.IO) {
                getProductList(shopId, ShopProductFilterInput().apply {
                    this.page = page
                    this.perPage = ShopPageConstant.DEFAULT_PER_PAGE
                    this.etalaseMenu = selectedEtalaseId
                    this.searchKeyword = ""
                    this.sort = 0
                })
            }
            productListData.postValue(Success(listShopProduct))
        }) {
            productListData.postValue(Fail(it))
        }
    }

    fun removeWishList(productId: String, listener: WishListActionListener) {
        removeWishlistUseCase.createObservable(productId, userId, listener)
    }

    fun addWishList(productId: String, listener: WishListActionListener) {
        addWishListUseCase.createObservable(productId, userId, listener)
    }

    fun getNewMembershipData(shopId: String) {
        launchCatchError(block = {
            val membershipStampProgressData = withContext(Dispatchers.IO) { getMembershipData(shopId) }
            newMembershipData.postValue(Success(membershipStampProgressData))
        }) {
            newMembershipData.postValue(Fail(it))
        }
    }

    fun getNewMerchantVoucher(shopId: String) {
        launchCatchError(block = {
            val merchantVoucherData = withContext(Dispatchers.IO) {
                getMerchantVoucherListData(shopId, NUM_VOUCHER_DISPLAY)
            }
            newMerchantVoucherData.postValue(Success(merchantVoucherData))
        }) {
            newMerchantVoucherData.postValue(Fail(it))
        }
    }

    fun clearCache() {
        deleteShopInfoUseCase.executeSync()
        clearMerchantVoucherCache()
        getShopEtalaseByShopUseCase.clearCache()
        getShopProductUseCase.get().clearCache()
        getShopFeaturedProductUseCase.clearCache()
    }

    fun clearMerchantVoucherCache() {
        getMerchantVoucherListUseCase.clearCache()
    }

    fun getEtalaseData(shopId: String, selectedEtalaseId: String) {
        launchCatchError(coroutineContext, block = {
            val etalaseListDataResult = withContext(Dispatchers.IO) { getShopEtalaseData(shopId, selectedEtalaseId) }
            etalaseListData.postValue(Success(etalaseListDataResult))
        }) {
            etalaseListData.postValue(Fail(it))
        }
    }
}