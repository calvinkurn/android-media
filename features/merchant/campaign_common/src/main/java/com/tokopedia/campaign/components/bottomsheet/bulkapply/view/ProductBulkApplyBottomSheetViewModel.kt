package com.tokopedia.campaign.components.bottomsheet.bulkapply.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyUiModel
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.ProductBulkApplyResult
import com.tokopedia.campaign.components.bottomsheet.bulkapply.data.uimodel.DiscountType
import java.util.*
import javax.inject.Inject

class ProductBulkApplyBottomSheetViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _inputProductDiscountValidation = MutableLiveData<ValidationState>()
    val inputProductDiscountValidation: LiveData<ValidationState>
        get() = _inputProductDiscountValidation

    private val _inputProductStockValidation = MutableLiveData<ValidationState>()
    val inputProductStockValidation: LiveData<ValidationState>
        get() = _inputProductStockValidation

    private val _isEnableApplyButton = MutableLiveData<Boolean>()
    val isEnableApplyButton: LiveData<Boolean>
        get() = _isEnableApplyButton

    private val _discountType = MutableLiveData<DiscountType>()
    val discountType: LiveData<DiscountType>
        get() = _discountType

    sealed class ValidationState {
        object InvalidDiscountMinimumPrice : ValidationState()
        object InvalidDiscountMaximumPrice : ValidationState()
        object InvalidDiscountPercentage : ValidationState()
        object InvalidStock : ValidationState()
        object Valid : ValidationState()
    }

    private var selectedStartDate: Date? = null
    private var selectedEndDate: Date? = null
    private var selectedDiscountType = DiscountType.RUPIAH
    private var selectedDiscountAmount = 0L
    private var selectedMaxQuantity = 0

    fun onDiscountTypeChanged(discountType: DiscountType) {
        this.selectedDiscountType = discountType
        _discountType.value = discountType
    }

    fun onDiscountAmountChanged(discountAmount: Long) {
        this.selectedDiscountAmount = discountAmount
    }

    fun onMaxPurchaseQuantityChanged(quantity: Int) {
        this.selectedMaxQuantity = quantity
    }

    fun getCurrentSelection(): ProductBulkApplyResult {
        return ProductBulkApplyResult(
            selectedStartDate,
            selectedEndDate,
            selectedDiscountType,
            selectedDiscountAmount,
            selectedMaxQuantity
        )
    }

    fun validateInputProductDiscount(
        bottomSheetConfigModel: ProductBulkApplyUiModel
    ) {
        if (selectedDiscountType == DiscountType.RUPIAH && selectedDiscountAmount < bottomSheetConfigModel.minimumDiscountPrice) {
            _inputProductDiscountValidation.value = ValidationState.InvalidDiscountMinimumPrice
            return
        }

        if (selectedDiscountType == DiscountType.RUPIAH && selectedDiscountAmount > bottomSheetConfigModel.maximumDiscountPrice) {
            _inputProductDiscountValidation.value = ValidationState.InvalidDiscountMaximumPrice
            return
        }

        if (selectedDiscountType == DiscountType.PERCENTAGE && selectedDiscountAmount !in bottomSheetConfigModel.minimumDiscountPercentage..bottomSheetConfigModel.maximumDiscountPercentage) {
            _inputProductDiscountValidation.value = ValidationState.InvalidDiscountPercentage
            return
        }
        _inputProductDiscountValidation.value = ValidationState.Valid
    }

    fun validateInputProductStock(bottomSheetConfigModel: ProductBulkApplyUiModel) {
        if (selectedMaxQuantity !in bottomSheetConfigModel.minimumStock..bottomSheetConfigModel.maximumStock) {
            _inputProductStockValidation.value = ValidationState.InvalidStock
            return
        }
        _inputProductStockValidation.value = ValidationState.Valid
    }

    fun checkApplyButton() {
        val isEnableApplyButton =
            _inputProductDiscountValidation.value == ValidationState.Valid && _inputProductStockValidation.value == ValidationState.Valid
        _isEnableApplyButton.postValue(isEnableApplyButton)
    }

}
