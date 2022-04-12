package com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountSettings
import com.tokopedia.shopdiscount.bulk.domain.entity.DiscountType
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductSubmissionResponse
import com.tokopedia.shopdiscount.common.data.response.DoSlashPriceProductReservationResponse
import com.tokopedia.shopdiscount.manage_discount.data.response.GetSlashPriceSetupProductListResponse
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceProductSubmissionUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSlashPriceStopUiModel
import com.tokopedia.shopdiscount.manage_discount.domain.GetSlashPriceSetupProductListUseCase
import com.tokopedia.shopdiscount.common.domain.MutationDoSlashPriceProductReservationUseCase
import com.tokopedia.shopdiscount.manage_discount.domain.MutationSlashPriceProductSubmissionUseCase
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMapper
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import java.util.*
import javax.inject.Inject
import kotlin.math.round

class ShopDiscountManageDiscountViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSlashPriceSetupProductListUseCase: GetSlashPriceSetupProductListUseCase,
    private val mutationSlashPriceProductSubmissionUseCase: MutationSlashPriceProductSubmissionUseCase,
    private val mutationDoSlashPriceProductReservationUseCase: MutationDoSlashPriceProductReservationUseCase
) : BaseViewModel(dispatcherProvider.main) {

    val setupProductListLiveData: LiveData<Result<ShopDiscountSetupProductUiModel>>
        get() = _setupProductListLiveData
    private val _setupProductListLiveData =
        MutableLiveData<Result<ShopDiscountSetupProductUiModel>>()

    val bulkApplyProductListResult: LiveData<List<ShopDiscountSetupProductUiModel.SetupProductData>>
        get() = _bulkApplyProductListResult
    private val _bulkApplyProductListResult =
        MutableLiveData<List<ShopDiscountSetupProductUiModel.SetupProductData>>()

    val enableButtonSubmitLiveData: LiveData<Boolean>
        get() = _enableButtonSubmitLiveData
    private val _enableButtonSubmitLiveData = MutableLiveData<Boolean>()

    val resultSubmitProductSlashPriceLiveData: LiveData<Result<ShopDiscountSlashPriceProductSubmissionUiModel>>
        get() = _resultSubmitProductSlashPriceLiveData
    private val _resultSubmitProductSlashPriceLiveData =
        MutableLiveData<Result<ShopDiscountSlashPriceProductSubmissionUiModel>>()

    val resultDeleteSlashPriceProductLiveData: LiveData<Result<ShopDiscountSlashPriceStopUiModel>>
        get() = _resultDeleteSlashPriceProductLiveData
    private val _resultDeleteSlashPriceProductLiveData =
        MutableLiveData<Result<ShopDiscountSlashPriceStopUiModel>>()

    fun getSetupProductListData(requestId: String) {
        launchCatchError(dispatcherProvider.io, block = {
            val response = getSetupProductListResponse(requestId)
            val mappedUiModel =
                ShopDiscountManageDiscountMapper.mapToShopDiscountSetupProductUiModel(
                    response.getSlashPriceSetupProductList
                )
            _setupProductListLiveData.postValue(Success(mappedUiModel))
        }) {
            _setupProductListLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getSetupProductListResponse(
        requestId: String
    ): GetSlashPriceSetupProductListResponse {
        getSlashPriceSetupProductListUseCase.setParams(
            ShopDiscountManageDiscountMapper.getSlashPriceSetupProductListRequestData(requestId)
        )
        return getSlashPriceSetupProductListUseCase.executeOnBackground()
    }

    fun applyBulkUpdate(
        listProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>,
        bulkApplyDiscountResult: DiscountSettings
    ) {
        listProductData.forEach { productData ->
            val minOriginalPrice = productData.mappedResultData.minOriginalPrice
            if (productData.productStatus.isVariant) {
                productData.listProductVariant.forEach { variantProductData ->
                    bulkApplyNonVariantProduct(
                        variantProductData,
                        bulkApplyDiscountResult,
                        minOriginalPrice
                    )
                }
            } else {
                bulkApplyNonVariantProduct(productData, bulkApplyDiscountResult, minOriginalPrice)
            }
            ShopDiscountManageDiscountMapper.updateProductStatusAndMappedData(productData)
        }
        _bulkApplyProductListResult.postValue(listProductData)
    }

    private fun bulkApplyNonVariantProduct(
        productData: ShopDiscountSetupProductUiModel.SetupProductData,
        bulkApplyDiscountResult: DiscountSettings,
        minOriginalPrice: Int
    ) {
        productData.slashPriceInfo.startDate = bulkApplyDiscountResult.startDate ?: Date()
        productData.slashPriceInfo.endDate = bulkApplyDiscountResult.endDate ?: Date()
        productData.listProductWarehouse.forEach {
            when (bulkApplyDiscountResult.discountType) {
                DiscountType.RUPIAH -> {
                    val diffPrice = minOriginalPrice - bulkApplyDiscountResult.discountAmount
                    val discountedPrice = it.originalPrice - diffPrice
                    val discountedPercent =
                        round(diffPrice.toDouble() / it.originalPrice.toDouble() * 100f)
                    it.discountedPrice = discountedPrice
                    it.discountedPercentage = discountedPercent.toInt()
                }
                DiscountType.PERCENTAGE -> {
                    val discountedPercent = bulkApplyDiscountResult.discountAmount
                    val discountedPrice = (100 - discountedPercent) * it.originalPrice / 100
                    it.discountedPrice = discountedPrice
                    it.discountedPercentage = discountedPercent
                }
            }
        }
    }

    fun checkShouldEnableButtonSubmit(allProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>) {
        val isEnableButtonSubmit = allProductData.all {
            it.productStatus.isProductDiscounted && !it.productStatus.isProductError
        }
        _enableButtonSubmitLiveData.postValue(isEnableButtonSubmit)
    }

    fun submitProductDiscount(
        listProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>,
        mode: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val action = when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    DoSlashPriceProductSubmissionRequest.DoSlashPriceSubmissionAction.CREATE
                }
                else -> {
                    DoSlashPriceProductSubmissionRequest.DoSlashPriceSubmissionAction.UPDATE
                }
            }
            val request = ShopDiscountManageDiscountMapper.mapToDoSlashPriceSubmissionRequest(
                listProductData,
                action
            )
            val response = submitSlashPriceProduct(request)
            val uiModel = ShopDiscountManageDiscountMapper.mapToSubmitSlashPriceUiModel(response)
            _resultSubmitProductSlashPriceLiveData.postValue(Success(uiModel))
        }) {
            _resultSubmitProductSlashPriceLiveData.postValue(Fail(it))
        }
    }

    private suspend fun submitSlashPriceProduct(request: DoSlashPriceProductSubmissionRequest): DoSlashPriceProductSubmissionResponse {
        mutationSlashPriceProductSubmissionUseCase.setParams(request)
        return mutationSlashPriceProductSubmissionUseCase.executeOnBackground()
    }

    fun deleteSlashPriceProduct(
        productId: String,
        position: String,
        requestId: String,
        mode: String
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            val state = when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    DoSlashPriceReservationRequest.DoSlashPriceReservationState.CREATE
                }
                else -> {
                    DoSlashPriceReservationRequest.DoSlashPriceReservationState.EDIT
                }
            }
            val request = ShopDiscountManageDiscountMapper.mapToDoSlashPriceStopRequest(
                productId,
                position,
                requestId,
                state
            )
            val response = getDoSlashPriceStopResponse(request)
            val uiModel =
                ShopDiscountManageDiscountMapper.mapToShopDiscountSlashPriceStopUiModel(response, productId)
            _resultDeleteSlashPriceProductLiveData.postValue(Success(uiModel))
        }) {
            _resultDeleteSlashPriceProductLiveData.postValue(Fail(it))
        }
    }

    private suspend fun getDoSlashPriceStopResponse(
        request: DoSlashPriceReservationRequest
    ): DoSlashPriceProductReservationResponse {
        mutationDoSlashPriceProductReservationUseCase.setParams(request)
        return mutationDoSlashPriceProductReservationUseCase.executeOnBackground()
    }

}