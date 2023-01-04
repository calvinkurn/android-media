package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.PageMode

data class VoucherCreationStepTwoUiState(
    val isLoading: Boolean = true,
    val originalPageMode: PageMode = PageMode.CREATE,
    val pageMode: PageMode = PageMode.CREATE,
    val voucherConfiguration: VoucherConfiguration = VoucherConfiguration(),
    val isVoucherNameError: Boolean = false,
    val voucherNameErrorMsg: String = "",
    val isVoucherCodeError: Boolean = false,
    val voucherCodeErrorMsg: String = "",
    val isStartDateError: Boolean = false,
    val startDateErrorMsg: String = "",
    val isEndDateError: Boolean = false,
    val endDateErrorMsg: String = "",
    val validationDate: List<VoucherValidationResult.ValidationDate> = emptyList(),
    val error: Throwable? = null
) {
    fun isInputValid(): Boolean = if (voucherConfiguration.isVoucherPublic) {
        if (voucherConfiguration.isPeriod){
            !isVoucherNameError && !isStartDateError && !isEndDateError && validationDate.any { it.available }
        } else {
            !isVoucherNameError && !isStartDateError && !isEndDateError
        }
    } else {
        if (voucherConfiguration.isPeriod) {
            !isVoucherNameError && !isVoucherCodeError && !isStartDateError && !isEndDateError && validationDate.any { it.available }
        } else {
            !isVoucherNameError && !isVoucherCodeError && !isStartDateError && !isEndDateError
        }
    }
}
