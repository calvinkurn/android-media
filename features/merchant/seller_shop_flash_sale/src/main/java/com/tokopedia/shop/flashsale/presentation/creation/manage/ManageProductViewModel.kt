package com.tokopedia.shop.flashsale.presentation.creation.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.di.GqlGetShopCloseDetailInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.flashsale.common.tracker.ShopFlashSaleTracker
import com.tokopedia.shop.flashsale.data.request.GetSellerCampaignProductListRequest
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.EMPTY_BANNER
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.ERROR_BANNER
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductBannerType.HIDE_BANNER
import com.tokopedia.shop.flashsale.domain.entity.enums.ManageProductErrorType.NOT_ERROR
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignDetailUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ManageProductMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ManageProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val userSessionInterface: UserSessionInterface,
    private val getSellerCampaignProductListUseCase: GetSellerCampaignProductListUseCase,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase,
    private val getSellerCampaignDetailUseCase: GetSellerCampaignDetailUseCase,
    private val tracker: ShopFlashSaleTracker,
    @GqlGetShopCloseDetailInfoQualifier private val gqlGetShopInfoUseCase: GQLGetShopInfoUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val ROWS = 50
        private const val OFFSET = 0
    }

    private val _products = MutableLiveData<Result<SellerCampaignProductList>>()
    val products: LiveData<Result<SellerCampaignProductList>>
        get() = _products

    private val _bannerType = MutableLiveData(HIDE_BANNER)
    val bannerType: LiveData<ManageProductBannerType>
        get() = _bannerType

    private val _removeProductsStatus = MutableLiveData<Result<Boolean>>()
    val removeProductsStatus: LiveData<Result<Boolean>>
        get() = _removeProductsStatus

    private var _shopStatus = MutableLiveData<Result<Int>>()
    val shopStatus: LiveData<Result<Int>>
        get() = _shopStatus

    val incompleteProducts = Transformations.map(products) {
        ManageProductMapper.filterInfoNotCompleted(it)
    }

    private var isCoachMarkShown = false

    var campaignName = ""
    var autoShowEditProduct = true
    private var autoNavigateWhenProductIsEmpty = true

    fun getProducts(
        campaignId: Long,
        listType: Int
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = getSellerCampaignProductListUseCase.execute(
                    campaignId = campaignId,
                    listType = listType,
                    pagination = GetSellerCampaignProductListRequest.Pagination(
                        ROWS,
                        OFFSET
                    )
                )
                _products.postValue(Success(campaigns))
            },
            onError = { error ->
                _products.postValue(Fail(error))
            }
        )
    }

    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignDetailUseCase.execute(campaignId)
                campaignName = result.campaignName
            },
            onError = {}
        )
    }

    fun getBannerType(productList: SellerCampaignProductList) {
        var isProductContainingError = false
        productList.productList.forEach { product ->
            val errorType = product.errorType
            if (product.isInfoComplete) {
                if (errorType != NOT_ERROR) {
                    _bannerType.postValue(ERROR_BANNER)
                    isProductContainingError = true
                }
            } else {
                if (bannerType.value != ERROR_BANNER) {
                    _bannerType.postValue(EMPTY_BANNER)
                    isProductContainingError = true
                }
            }
        }
        if (!isProductContainingError) {
            _bannerType.postValue(HIDE_BANNER)
        }
    }

    fun isProductInfoComplete(productMapData: SellerCampaignProductList.ProductMapData): Boolean {
        return when {
            productMapData.discountedPrice.isZero() -> false
            productMapData.discountPercentage.isZero() -> false
            productMapData.originalCustomStock.isZero() -> false
            productMapData.customStock.isZero() -> false
            productMapData.maxOrder.isZero() -> false
            else -> true
        }
    }

    fun removeProducts(
        campaignId: Long,
        productList: List<SellerCampaignProductList.Product>
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId = campaignId.toString(),
                    productData = ManageProductMapper.mapToProductDataList(productList),
                    action = ProductionSubmissionAction.DELETE
                )
                _removeProductsStatus.postValue(Success(campaigns.isSuccess))
            },
            onError = { error ->
                _removeProductsStatus.postValue(Fail(error))
            }
        )
    }

    fun getShopStatus() {
        launchCatchError(
            dispatchers.io,
            block = {
                val shopId = userSessionInterface.shopId.toIntOrZero()
                gqlGetShopInfoUseCase.params = GQLGetShopInfoUseCase.createParams(listOf(shopId))
                gqlGetShopInfoUseCase.isFromCacheFirst = false
                val result = gqlGetShopInfoUseCase.executeOnBackground()
                _shopStatus.postValue(Success(result.statusInfo.shopStatus))
            },
            onError = { error ->
                _shopStatus.postValue(Fail(error))
            }
        )
    }

    fun onButtonProceedTapped() {
        tracker.sendClickButtonProceedOnManageProductPage()
    }

    fun setIsCoachMarkShown(isShown: Boolean) {
        this.isCoachMarkShown = isShown
    }

    fun getIsCoachMarkShown(): Boolean {
        return isCoachMarkShown
    }

    fun setAutoNavigateToChooseProduct(value: Boolean) {
        autoNavigateWhenProductIsEmpty = value
    }

    fun autoNavigateToChooseProduct(): Boolean {
        return autoNavigateWhenProductIsEmpty
    }
}
