package com.tokopedia.shopdiscount.manage_discount.presentation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
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
                ).apply {
                    listSetupProductData.forEach { setupProductData ->
                        updateProductStatusAndMappedData(setupProductData)
                    }
                }
            _setupProductListLiveData.postValue(Success(mappedUiModel))
        }) {
            _setupProductListLiveData.postValue(Fail(it))
        }
    }

    private fun updateProductStatusAndMappedData(setupProductData: ShopDiscountSetupProductUiModel.SetupProductData) {
        setupProductData.productStatus = setToProductStatus(setupProductData)
        setupProductData.mappedResultData = mapToMappedResultData(setupProductData)
    }

    private fun setToProductStatus(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus {
        val isVariant = isVariant(setupProductUiModel)
        var isProductDiscounted = getIsProductDiscounted(setupProductUiModel, isVariant)
        val isMultiLoc = isMultiLoc(setupProductUiModel, isVariant)
        val errorType = setProductError(
            setupProductUiModel,
            isVariant,
            isProductDiscounted
        )
        if (errorType != ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus.ErrorType.NO_ERROR) {
            isProductDiscounted = false
        }
        return ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
            isProductDiscounted = isProductDiscounted,
            isVariant = isVariant,
            isMultiLoc = isMultiLoc,
            errorType = errorType
        )
    }

    private fun mapToMappedResultData(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData {
        return ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
            minOriginalPrice = getMinOriginalPrice(setupProductUiModel),
            maxOriginalPrice = getMaxOriginalPrice(setupProductUiModel),
            minDisplayedPrice = getMinDisplayedPrice(setupProductUiModel),
            maxDisplayedPrice = getMaxDisplayedPrice(setupProductUiModel),
            minDiscountPercentage = getMinDiscountPercentage(setupProductUiModel),
            maxDiscountPercentage = getMaxDiscountPercentage(setupProductUiModel),
            totalVariant = getTotalVariant(setupProductUiModel),
            totalDiscountedVariant = getTotalDiscountedVariant(setupProductUiModel),
            totalLocation = getTotalLocation(setupProductUiModel)
        )
    }

    private fun setProductError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean,
        isProductDiscounted: Boolean
    ): Int {
        return when {
            isAllAbusiveError(setupProductUiModel, isVariant) -> {
                ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus.ErrorType.ALL_ABUSIVE_ERROR
            }
            isPartialAbusiveError(
                setupProductUiModel,
                isVariant
            ) -> {
                ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus.ErrorType.PARTIAL_ABUSIVE_ERROR
            }
            isValueError(setupProductUiModel, isVariant) && isProductDiscounted -> {
                ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus.ErrorType.VALUE_ERROR
            }
            else -> {
                ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus.ErrorType.NO_ERROR
            }
        }
    }

    private fun isValueError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean
    ): Boolean {
        return if (isVariant) {
            setupProductUiModel.listProductVariant.any {
                checkProductValueError(it)
            }
        } else {
            checkProductValueError(setupProductUiModel)
        }
    }

    private fun checkProductValueError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return setupProductUiModel.listProductWarehouse.any {
            it.discountedPercentage < 1 || it.discountedPercentage > 99
        }
    }

    private fun isPartialAbusiveError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean
    ): Boolean {
        return if (isVariant) {
            setupProductUiModel.listProductVariant.any {
                checkProductWarehousePartialAbusiveRuleError(it)
            }
        } else {
            checkProductWarehousePartialAbusiveRuleError(setupProductUiModel)
        }
    }

    private fun checkProductWarehousePartialAbusiveRuleError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return setupProductUiModel.listProductWarehouse.any {
            it.abusiveRule
        }
    }

    private fun isAllAbusiveError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean
    ): Boolean {
        return if (isVariant) {
            setupProductUiModel.listProductVariant.all {
                checkProductWarehouseAllAbusiveRuleError(it)
            }
        } else {
            checkProductWarehouseAllAbusiveRuleError(setupProductUiModel)
        }
    }

    private fun checkProductWarehouseAllAbusiveRuleError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return setupProductUiModel.listProductWarehouse.all {
            it.abusiveRule
        }
    }

    private fun getTotalLocation(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return setupProductUiModel.listProductWarehouse.size
    }

    private fun getTotalDiscountedVariant(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return setupProductDataUiModel.listProductVariant.count {
            it.listProductWarehouse.any { productWarehouse ->
                !productWarehouse.discountedPercentage.isZero()
            }
        }
    }

    private fun getTotalVariant(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return setupProductDataUiModel.listProductVariant.size
    }

    private fun getIsProductDiscounted(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean
    ): Boolean {
        return if (isVariant) {
            setupProductDataUiModel.listProductVariant.any {
                it.listProductWarehouse.any { productWarehouse ->
                    !productWarehouse.discountedPrice.isZero()
                }
            }
        } else {
            setupProductDataUiModel.listProductWarehouse.any {
                !it.discountedPercentage.isZero()
            }
        }
    }

    private fun isMultiLoc(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean
    ): Boolean {
        return if (isVariant) {
            setupProductDataUiModel.listProductVariant.any {
                it.listProductWarehouse.size > 1
            }
        } else {
            setupProductDataUiModel.listProductWarehouse.size > 1
        }
    }

    private fun getMaxOriginalPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.originalPrice
                }.maxOrNull().orZero()
            }.maxOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.originalPrice
            }.maxOrNull().orZero()
        }
    }

    private fun getMinOriginalPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.originalPrice
                }.minOrNull().orZero()
            }.minOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.originalPrice
            }.minOrNull().orZero()
        }
    }

    private fun getMaxDisplayedPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPrice
                }.maxOrNull().orZero()
            }.maxOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPrice
            }.maxOrNull().orZero()
        }
    }

    private fun getMinDisplayedPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPrice
                }.minOrNull().orZero()
            }.minOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPrice
            }.minOrNull().orZero()
        }
    }

    private fun getMaxDiscountPercentage(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPercentage
                }.maxOrNull().orZero()
            }.maxOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPercentage
            }.maxOrNull().orZero()
        }
    }

    private fun getMinDiscountPercentage(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Int {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            setupProductDataUiModel.listProductVariant.map {
                it.listProductWarehouse.map { productWarehouse ->
                    productWarehouse.discountedPercentage
                }.minOrNull().orZero()
            }.minOrNull().orZero()
        } else {
            setupProductDataUiModel.listProductWarehouse.map {
                it.discountedPercentage
            }.minOrNull().orZero()
        }
    }

    private fun isVariant(setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData): Boolean {
        return setupProductUiModel.listProductVariant.isNotEmpty()
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
        launchCatchError(dispatcherProvider.io, {
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
                    bulkApplyNonVariantProduct(
                        productData,
                        bulkApplyDiscountResult,
                        minOriginalPrice
                    )
                }
                updateProductStatusAndMappedData(productData)
            }
            _bulkApplyProductListResult.postValue(listProductData)
        }) {}
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
            it.maxOrder = bulkApplyDiscountResult.maxPurchaseQuantity.toString()
        }
    }

    fun checkShouldEnableButtonSubmit(allProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>) {
        val isEnableButtonSubmit = allProductData.all {
            it.productStatus.isProductDiscounted && it.productStatus.errorType == ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus.ErrorType.NO_ERROR
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
                ShopDiscountManageDiscountMapper.mapToShopDiscountSlashPriceStopUiModel(
                    response,
                    productId
                )
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