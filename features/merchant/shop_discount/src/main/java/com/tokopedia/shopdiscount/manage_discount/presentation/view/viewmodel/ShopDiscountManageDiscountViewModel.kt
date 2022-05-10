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
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.ALL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.NO_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.PARTIAL_ABUSIVE_ERROR
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel.SetupProductData.ErrorType.Companion.VALUE_ERROR
import com.tokopedia.shopdiscount.manage_discount.domain.MutationSlashPriceProductSubmissionUseCase
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMapper
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.utils.constant.SlashPriceStatusId
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.delay
import java.util.*
import javax.inject.Inject
import kotlin.math.round

class ShopDiscountManageDiscountViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSlashPriceSetupProductListUseCase: GetSlashPriceSetupProductListUseCase,
    private val mutationSlashPriceProductSubmissionUseCase: MutationSlashPriceProductSubmissionUseCase,
    private val mutationDoSlashPriceProductReservationUseCase: MutationDoSlashPriceProductReservationUseCase
) : BaseViewModel(dispatcherProvider.main) {

    companion object {
        private const val GET_PRODUCT_LIST_DELAY: Long = 1000
    }

    val setupProductListLiveData: LiveData<Result<ShopDiscountSetupProductUiModel>>
        get() = _setupProductListLiveData
    private val _setupProductListLiveData =
        MutableLiveData<Result<ShopDiscountSetupProductUiModel>>()

    val updatedProductListData: LiveData<List<ShopDiscountSetupProductUiModel.SetupProductData>>
        get() = _updatedProductListData
    private val _updatedProductListData =
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

    fun getSetupProductListData(
        requestId: String,
        selectedProductVariantId: String,
        mode: String,
        slashPriceStatus: Int
    ) {
        launchCatchError(dispatcherProvider.io, block = {
            delay(GET_PRODUCT_LIST_DELAY)
            val response = getSetupProductListResponse(requestId)
            val mappedUiModel =
                ShopDiscountManageDiscountMapper.mapToShopDiscountSetupProductUiModel(
                    response.getSlashPriceSetupProductList,
                    selectedProductVariantId
                ).apply {
                    listSetupProductData.forEach { setupProductData ->
                        updateProductStatusAndMappedData(setupProductData, mode, slashPriceStatus)
                    }
                }
            _setupProductListLiveData.postValue(Success(mappedUiModel))
        }) {
            _setupProductListLiveData.postValue(Fail(it))
        }
    }

    private fun updateProductStatusAndMappedData(
        setupProductData: ShopDiscountSetupProductUiModel.SetupProductData,
        mode: String,
        slashPriceStatus: Int
    ) {
        setupProductData.productStatus = setToProductStatus(setupProductData, slashPriceStatus)
        setupProductData.listProductVariant.forEach {
            it.variantStatus = mapToVariantStatus(it, mode)
        }
        setupProductData.mappedResultData =
            mapToMappedResultData(setupProductData, slashPriceStatus)
    }

    private fun mapToVariantStatus(
        setupProductData: ShopDiscountSetupProductUiModel.SetupProductData,
        mode: String
    ): ShopDiscountSetupProductUiModel.SetupProductData.VariantStatus {
        return ShopDiscountSetupProductUiModel.SetupProductData.VariantStatus(
            isMultiLoc = isMultiLoc(setupProductData, false),
            isVariantEnabled = getIsVariantEnabled(
                setupProductData.variantStatus.isVariantEnabled,
                mode
            )
        )
    }

    private fun getIsVariantEnabled(isVariantEnabled: Boolean?, mode: String): Boolean {
        return isVariantEnabled
            ?: when (mode) {
                ShopDiscountManageDiscountMode.CREATE -> {
                    false
                }
                ShopDiscountManageDiscountMode.UPDATE -> {
                    true
                }
                else -> {
                    false
                }
            }
    }

    private fun setToProductStatus(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        slashPriceStatus: Int
    ): ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus {
        val isVariant = isVariant(setupProductUiModel)
        var isProductDiscounted = getIsProductDiscounted(setupProductUiModel, isVariant)
        val isMultiLoc = isMultiLoc(setupProductUiModel, isVariant)
        val errorType = setProductError(
            setupProductUiModel,
            isVariant,
            isProductDiscounted
        )
        if (errorType != NO_ERROR) {
            isProductDiscounted = false
        }
        return ShopDiscountSetupProductUiModel.SetupProductData.ProductStatus(
            isProductDiscounted = isProductDiscounted,
            isVariant = isVariant,
            isMultiLoc = isMultiLoc,
            errorType = errorType,
            slashPriceStatus = slashPriceStatus
        )
    }

    private fun mapToMappedResultData(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        slashPriceStatus: Int
    ): ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData {
        val minStartDateUnix = getStartDate(setupProductUiModel).takeIf {
            it.all { it > 0 }
        }?.minOrNull()
        val minEndDateUnix = getEndDate(setupProductUiModel).takeIf {
            it.all { it > 0 }
        }?.minOrNull()
        return ShopDiscountSetupProductUiModel.SetupProductData.MappedResultData(
            minOriginalPrice = getOriginalPrice(setupProductUiModel).minOrNull().orZero(),
            maxOriginalPrice = getOriginalPrice(setupProductUiModel).maxOrNull().orZero(),
            minDisplayedPrice = getDisplayedPrice(setupProductUiModel).minOrNull().orZero(),
            maxDisplayedPrice = getDisplayedPrice(setupProductUiModel).maxOrNull().orZero(),
            minDiscountPercentage = getDiscountPercentage(setupProductUiModel).minOrNull().orZero(),
            maxDiscountPercentage = getDiscountPercentage(setupProductUiModel).maxOrNull().orZero(),
            totalVariant = getTotalVariant(setupProductUiModel),
            totalDiscountedVariant = getTotalDiscountedVariant(setupProductUiModel),
            totalLocation = getTotalLocation(setupProductUiModel),
            minStartDateUnix = minStartDateUnix,
            minEndDateUnix = minEndDateUnix
        )
    }

    private fun getStartDate(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<Long> {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            mutableListOf<Long>().apply {
                setupProductDataUiModel.listProductVariant.forEach {
                    add(it.slashPriceInfo.startDate.time)
                }
            }.toList()
        } else {
            listOf(setupProductDataUiModel.slashPriceInfo.startDate.time)
        }
    }

    private fun getEndDate(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<Long> {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            mutableListOf<Long>().apply {
                setupProductDataUiModel.listProductVariant.forEach {
                    add(it.slashPriceInfo.endDate.time)
                }
            }.toList()
        } else {
            listOf(setupProductDataUiModel.slashPriceInfo.endDate.time)
        }
    }

    private fun setProductError(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData,
        isVariant: Boolean,
        isProductDiscounted: Boolean
    ): Int {
        return when {
            isAllAbusiveError(setupProductUiModel, isVariant) -> {
                ALL_ABUSIVE_ERROR
            }
            isPartialAbusiveError(
                setupProductUiModel,
                isVariant
            ) -> {
                PARTIAL_ABUSIVE_ERROR
            }
            isValueError(setupProductUiModel, isVariant) && isProductDiscounted -> {
                VALUE_ERROR
            }
            else -> {
                NO_ERROR
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
        return setupProductUiModel.getListEnabledProductWarehouse().any {
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
        return setupProductUiModel.getListEnabledProductWarehouse().any {
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
        return setupProductUiModel.getListEnabledProductWarehouse().all {
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
                getIsProductDiscountedBasedOnProductWarehouse(it)
            }
        } else {
            getIsProductDiscountedBasedOnProductWarehouse(setupProductDataUiModel)
        }
    }

    private fun getIsProductDiscountedBasedOnProductWarehouse(
        setupProductUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): Boolean {
        return setupProductUiModel.listProductWarehouse.any {
            !it.discountedPercentage.isZero()
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

    private fun getOriginalPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<Int> {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            mutableListOf<Int>().apply {
                setupProductDataUiModel.listProductVariant.forEach {
                    addAll(getOriginalPriceFromProductWarehouse(it))
                }
            }.toList()
        } else {
            getOriginalPriceFromProductWarehouse(setupProductDataUiModel)
        }
    }

    private fun getOriginalPriceFromProductWarehouse(setupProductData: ShopDiscountSetupProductUiModel.SetupProductData): List<Int> {
        return setupProductData.getListEnabledProductWarehouse().map {
            it.originalPrice
        }
    }

    private fun getDisplayedPrice(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<Int> {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            mutableListOf<Int>().apply {
                setupProductDataUiModel.listProductVariant.forEach {
                    addAll(getDisplayedPriceFromProductWarehouse(it))
                }
            }.toList()
        } else {
            getDisplayedPriceFromProductWarehouse(setupProductDataUiModel)
        }
    }

    private fun getDisplayedPriceFromProductWarehouse(setupProductData: ShopDiscountSetupProductUiModel.SetupProductData): List<Int> {
        return setupProductData.getListEnabledProductWarehouse().map {
            it.discountedPrice
        }
    }

    private fun getDiscountPercentage(
        setupProductDataUiModel: ShopDiscountSetupProductUiModel.SetupProductData
    ): List<Int> {
        return if (setupProductDataUiModel.productStatus.isVariant) {
            mutableListOf<Int>().apply {
                setupProductDataUiModel.listProductVariant.forEach {
                    addAll(getDiscountPercentageFromProductWarehouse(it))
                }
            }.toList()
        } else {
            getDiscountPercentageFromProductWarehouse(setupProductDataUiModel)
        }
    }

    private fun getDiscountPercentageFromProductWarehouse(setupProductData: ShopDiscountSetupProductUiModel.SetupProductData): List<Int> {
        return setupProductData.getListEnabledProductWarehouse().map {
            it.discountedPercentage
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
        bulkApplyDiscountResult: DiscountSettings,
        mode: String,
        slashPriceStatus: Int
    ) {
        launchCatchError(dispatcherProvider.io, {
            listProductData.forEach { productData ->
                val minOriginalPrice = productData.mappedResultData.minOriginalPrice
                if (productData.productStatus.isVariant) {
                    productData.listProductVariant.forEach { variantProductData ->
                        updateProductData(
                            variantProductData,
                            bulkApplyDiscountResult,
                            minOriginalPrice,
                            slashPriceStatus
                        )
                    }
                } else {
                    updateProductData(
                        productData,
                        bulkApplyDiscountResult,
                        minOriginalPrice,
                        slashPriceStatus
                    )
                }
                updateProductStatusAndMappedData(productData, mode, slashPriceStatus)
            }
            _updatedProductListData.postValue(listProductData)
        }) {}
    }

    private fun updateProductData(
        productData: ShopDiscountSetupProductUiModel.SetupProductData,
        bulkApplyDiscountResult: DiscountSettings,
        minOriginalPrice: Int,
        slashPriceStatus: Int
    ) {
        val discountedPrice: Int
        val discountedPercentage: Int
        when (bulkApplyDiscountResult.discountType) {
            DiscountType.RUPIAH -> {
                val diffPrice = minOriginalPrice - bulkApplyDiscountResult.discountAmount
                discountedPrice = minOriginalPrice - diffPrice
                discountedPercentage =
                    round(diffPrice.toDouble() / minOriginalPrice.toDouble() * 100f).toInt()
            }
            DiscountType.PERCENTAGE -> {
                discountedPercentage = bulkApplyDiscountResult.discountAmount
                discountedPrice = (100 - discountedPercentage) * minOriginalPrice / 100
            }
        }
        when (slashPriceStatus) {
            SlashPriceStatusId.CREATE, SlashPriceStatusId.SCHEDULED -> {
                productData.slashPriceInfo.startDate = bulkApplyDiscountResult.startDate ?: Date()
            }
        }
        productData.slashPriceInfo.endDate = bulkApplyDiscountResult.endDate ?: Date()
        productData.slashPriceInfo.discountedPrice = discountedPrice
        productData.slashPriceInfo.discountPercentage = discountedPercentage
        productData.getListEnabledProductWarehouse().forEach {
            it.discountedPrice = discountedPrice
            it.discountedPercentage = discountedPercentage
            it.maxOrder = bulkApplyDiscountResult.maxPurchaseQuantity.toString()
        }
    }

    fun checkShouldEnableButtonSubmit(allProductData: List<ShopDiscountSetupProductUiModel.SetupProductData>) {
        val isEnableButtonSubmit = allProductData.all {
            it.productStatus.isProductDiscounted && it.productStatus.errorType == NO_ERROR
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

    fun updateSingleProductData(
        currentProductListData: List<ShopDiscountSetupProductUiModel.SetupProductData>,
        newProdData: ShopDiscountSetupProductUiModel.SetupProductData,
        mode: String,
        slashPriceStatus: Int
    ) {
        val matched = currentProductListData.firstOrNull {
            it.productId == newProdData.productId
        }
        val newList = currentProductListData.toMutableList()
        newList[currentProductListData.indexOf(matched)] = newProdData
        updateProductStatusAndMappedData(
            newList[currentProductListData.indexOf(matched)],
            mode,
            slashPriceStatus
        )
        _updatedProductListData.postValue(newList.toList())
    }

}