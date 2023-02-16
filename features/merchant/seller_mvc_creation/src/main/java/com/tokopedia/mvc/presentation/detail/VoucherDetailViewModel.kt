package com.tokopedia.mvc.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.mvc.data.response.UpdateStatusVoucherDataModel
import com.tokopedia.mvc.domain.entity.GenerateVoucherImageMetadata
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import com.tokopedia.mvc.domain.entity.VoucherDetailWithVoucherCreationMetadata
import com.tokopedia.mvc.domain.entity.enums.UpdateVoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherAction
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.mvc.domain.usecase.CancelVoucherUseCase
import com.tokopedia.mvc.domain.usecase.GetInitiateVoucherPageUseCase
import com.tokopedia.mvc.domain.usecase.MerchantPromotionGetMVDataByIDUseCase
import com.tokopedia.mvc.domain.usecase.ProductListUseCase
import com.tokopedia.mvc.domain.usecase.ShopBasicDataUseCase
import com.tokopedia.mvc.util.constant.NumberConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.async
import javax.inject.Inject
import kotlin.math.roundToInt

class VoucherDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val merchantPromotionGetMVDataByIDUseCase: MerchantPromotionGetMVDataByIDUseCase,
    private val getProductsUseCase: ProductListUseCase,
    private val getInitiateVoucherPageUseCase: GetInitiateVoucherPageUseCase,
    private val cancelVoucherUseCase: CancelVoucherUseCase,
    private val shopBasicDataUseCase: ShopBasicDataUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val DEFAULT_PERCENTAGE_NORMALIZATION = 100
        private const val THREE_TOP_SELLING_PRODUCT = 3
    }

    private var _voucherDetail = MutableLiveData<Result<VoucherDetailWithVoucherCreationMetadata>>()
    val voucherDetail: LiveData<Result<VoucherDetailWithVoucherCreationMetadata>>
        get() = _voucherDetail

    private val _generateVoucherImageMetadata =
        MutableLiveData<Result<GenerateVoucherImageMetadata>>()
    val generateVoucherImageMetadata: LiveData<Result<GenerateVoucherImageMetadata>>
        get() = _generateVoucherImageMetadata

    private val _openDownloadVoucherImageBottomSheet = MutableLiveData<VoucherDetailData>()
    val openDownloadVoucherImageBottomSheet: LiveData<VoucherDetailData>
        get() = _openDownloadVoucherImageBottomSheet

    private val _redirectToProductListPage = MutableLiveData<VoucherDetailData>()
    val redirectToProductListPage: LiveData<VoucherDetailData>
        get() = _redirectToProductListPage

    private val _updateVoucherStatusData = MutableLiveData<Result<UpdateStatusVoucherDataModel>>()
    val updateVoucherStatusData: LiveData<Result<UpdateStatusVoucherDataModel>>
        get() = _updateVoucherStatusData

    fun getVoucherDetail(voucherId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {

                val voucherCreationMetadataDeferred = async { getInitiateVoucherPageUseCase.execute() }

                val voucherDetailParam = MerchantPromotionGetMVDataByIDUseCase.Param(voucherId)
                val voucherDetailDeferred = async { merchantPromotionGetMVDataByIDUseCase.execute(voucherDetailParam) }

                val voucherCreationMetadata = voucherCreationMetadataDeferred.await()
                val voucherDetail = voucherDetailDeferred.await()


                val data = VoucherDetailWithVoucherCreationMetadata(
                    voucherDetail = voucherDetail,
                    creationMetadata = voucherCreationMetadata,
                    tickerWording = ""
                )

                _voucherDetail.postValue(Success(data))
            },
            onError = { error ->
                _voucherDetail.postValue(Fail(error))
            }
        )
    }

    fun updateVoucherStatus(data: VoucherDetailData) {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadataParam = GetInitiateVoucherPageUseCase.Param(
                    action = VoucherAction.UPDATE,
                    promoType = data.voucherType,
                    isVoucherProduct = data.isVoucherProduct
                )
                val token = getInitiateVoucherPageUseCase.execute(metadataParam).token
                val couponStatus = if (data.voucherStatus == VoucherStatus.NOT_STARTED) UpdateVoucherAction.DELETE else UpdateVoucherAction.STOP
                val response = cancelVoucherUseCase.execute(data.voucherId.toInt(), couponStatus, token)
                _updateVoucherStatusData.postValue(Success(response))
            },
            onError = { error ->
                _updateVoucherStatusData.value = Fail(error)
            }
        )
    }

    fun getSpendingEstimation(data: VoucherDetailData): String {
        val voucherDiscount = data.voucherDiscountAmount
        val voucherQuota = data.voucherQuota
        return (voucherDiscount * voucherQuota).getCurrencyFormatted()
    }

    fun getPercentage(availableQuota: Long, remainingQuota: Long): Int {
        return if (remainingQuota.isZero()) {
            Int.ZERO
        } else {
            ((availableQuota.toDouble() / remainingQuota.toDouble()) * DEFAULT_PERCENTAGE_NORMALIZATION).roundToInt()
        }
    }

    fun getThreeDotsBottomSheetType(data: VoucherDetailData): VoucherStatus {
        return data.voucherStatus
    }

    fun generateVoucherImage() {
        launchCatchError(
            dispatchers.io,
            block = {
                val voucherDetail = _voucherDetail.value?.unwrapOrNull() ?: return@launchCatchError
                val parentProductIds = voucherDetail.productIds.map { it.parentProductId }
                val shopDataDeferred = async { shopBasicDataUseCase.execute() }

                val productListParam = ProductListUseCase.Param(
                    searchKeyword = "",
                    warehouseId = 0,
                    categoryIds = emptyList(),
                    showcaseIds = emptyList(),
                    page = NumberConstant.FIRST_PAGE,
                    pageSize = parentProductIds.size,
                    sortId = "DEFAULT",
                    sortDirection = "DESC",
                    productIdInclude = parentProductIds
                )

                val productsDeferred = async { getProductsUseCase.execute(productListParam) }

                val shopData = shopDataDeferred.await()
                val products = productsDeferred.await()

                val topSellingProductImageUrls = products.products
                    .sortedByDescending { it.txStats.sold }
                    .take(THREE_TOP_SELLING_PRODUCT)
                    .map { it.picture }

                val metadata = GenerateVoucherImageMetadata(
                    voucherDetail,
                    shopData,
                    topSellingProductImageUrls
                )
                _generateVoucherImageMetadata.postValue(Success(metadata))
            },
            onError = { error ->
                _generateVoucherImageMetadata.postValue(Fail(error))
            }
        )
    }

    fun onTapDownloadVoucherImage() {
        val voucherDetail = _voucherDetail.value?.unwrapOrNull() ?: return
        _openDownloadVoucherImageBottomSheet.value = voucherDetail
    }

    fun onTapViewAllProductCta() {
        val voucherDetail = _voucherDetail.value?.unwrapOrNull() ?: return
        _redirectToProductListPage.value = voucherDetail
    }

    private fun Result<VoucherDetailWithVoucherCreationMetadata>.unwrapOrNull(): VoucherDetailData? {
        return if (this is Success) {
            this.data.voucherDetail
        } else {
            null
        }
    }
}
