package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.DiscountUtil
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.helper.ErrorMessageHelper
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.uimodel.ValidationResult
import com.tokopedia.tkpd.flashsale.util.tracker.ManageProductVariantTracker
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.SINGLE_LOCATION
import com.tokopedia.tkpd.flashsale.util.constant.TrackerConstant.MULTI_LOCATION
import javax.inject.Inject

class ManageProductVariantViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val errorMessageHelper: ErrorMessageHelper,
    private val getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase,
    private val tracker: ManageProductVariantTracker
) : BaseViewModel(dispatchers.main) {

    private var productData = ReservedProduct.Product()

    private val _isInputPageValid: MutableLiveData<Boolean> = MutableLiveData()
    val isInputPageValid: LiveData<Boolean>
        get() = _isInputPageValid

    private val _criteriaCheckingResult = MutableLiveData<List<CriteriaCheckingResult>>()
    val criteriaCheckingResult: LiveData<List<CriteriaCheckingResult>> get() = _criteriaCheckingResult

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private var campaignId = 0L

    fun validateInput(
        criteria: ReservedProduct.Product.ProductCriteria,
        discountSetup: ReservedProduct.Product.Warehouse.DiscountSetup
    ): ValidationResult {
        return ValidationResult(
            isPriceError = discountSetup.price !in criteria.minFinalPrice..criteria.maxFinalPrice,
            isPricePercentError = discountSetup.discount !in criteria.minDiscount..criteria.maxDiscount,
            isStockError = discountSetup.stock !in criteria.minCustomStock..criteria.maxCustomStock,
            priceMessage = errorMessageHelper.getPriceMessage(criteria, discountSetup),
            pricePercentMessage = errorMessageHelper.getDiscountMessage(criteria, discountSetup)
        )
    }

    fun checkCriteria(
        item: ReservedProduct.Product.ChildProduct,
        productCriteria: ReservedProduct.Product.ProductCriteria
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleProductCriteriaCheckingUseCase.execute(
                    productId = item.productId,
                    campaignId = campaignId,
                    productCriteriaId = productCriteria.criteriaId
                )
                _criteriaCheckingResult.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun setupInitiateProductData(product: ReservedProduct.Product) {
        productData = product
    }

    fun getProductData(): ReservedProduct.Product {
        return productData
    }

    fun setItemToggleValue(itemPosition: Int, value: Boolean) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.isToggleOn = value
        selectedItem.warehouses.map {
            it.isToggleOn = value
        }
    }

    fun setDiscountValue(itemPosition: Int, priceValue: Long, discountValue: Int) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.warehouses.map { warehouse ->
            warehouse.discountSetup.price = priceValue
            warehouse.discountSetup.discount = discountValue
        }
    }

    fun setStockValue(itemPosition: Int, stockValue: Long) {
        val selectedItem = productData.childProducts[itemPosition]
        selectedItem.warehouses.map { warehouse ->
            warehouse.discountSetup.stock = stockValue
        }
    }

    fun setCampaignId(campaignId: String) {
        this.campaignId = campaignId.toLong()
    }

    fun validateInputPage(
        criteria: ReservedProduct.Product.ProductCriteria
    ) {
        if (productData.childProducts.any { it.isToggleOn && !it.isDisabled }) {
            _isInputPageValid.value = productData.childProducts
                .filter { it.isToggleOn }
                .all { childProduct ->
                    if (childProduct.warehouses.any { it.isToggleOn && !it.isDisabled }) {
                        childProduct.warehouses
                            .filter { warehouse -> warehouse.isToggleOn && !warehouse.isDisabled }
                            .all { warehouse ->
                                validateInput(
                                    criteria,
                                    warehouse.discountSetup
                                ).isAllFieldValid()
                            }
                    } else {
                        false
                    }
                }
        } else {
            _isInputPageValid.value = false
        }
    }

    fun calculatePrice(percentInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePrice(percentInput, originalPrice).toString()
    }

    fun calculatePercent(priceInput: Long, originalPrice: Long): String {
        return DiscountUtil.calculatePercent(priceInput, originalPrice).toString()
    }

    fun sendManageAllClickEvent(campaignId: String) {
        val trackerLabel = "$campaignId - $SINGLE_LOCATION"
        tracker.sendClickManageAllEvent(trackerLabel)
    }

    fun sendAdjustToggleVariantEvent(campaignId: String) {
        val trackerLabel = "$campaignId - $SINGLE_LOCATION"
        tracker.sendAdjustToggleVariantEvent(trackerLabel)
    }

    fun sendFillInColumnPriceEvent(campaignId: String) {
        val trackerLabel = "$campaignId - $SINGLE_LOCATION"
        tracker.sendClickFillInCampaignPriceEvent(trackerLabel)
    }

    fun sendFillInDiscountPercentageEvent(campaignId: String) {
        val trackerLabel = "$campaignId - $SINGLE_LOCATION"
        tracker.sendClickFillInDiscountPercentageEvent(trackerLabel)
    }

    fun sendSaveClickEvent(campaignId: String) {
        val trackerLabel = "$campaignId - $SINGLE_LOCATION"
        tracker.sendClickSaveEvent(trackerLabel)
    }

    fun sendCheckDetailClickEvent(campaignId: String, productId: Long) {
        val trackerLabel = "$campaignId - $productId - $SINGLE_LOCATION"
        tracker.sendClickCheckDetailEvent(trackerLabel)
    }

    fun sendManageAllLocationClickEvent(campaignId: String, productId: Long) {
        val trackerLabel = "$campaignId - $productId - $MULTI_LOCATION"
        tracker.sendClickManageAllLocationEvent(trackerLabel)
    }
}
