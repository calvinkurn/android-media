package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.VoucherValidationResult
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.VoucherCreationStepTwoFieldValidation

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
    val fieldValidated: VoucherCreationStepTwoFieldValidation = VoucherCreationStepTwoFieldValidation.ALL,
    val error: Throwable? = null
) {
    fun isInputValid(): Boolean = if (voucherConfiguration.isVoucherPublic) {
        validateWhenVoucherIsPublic()
    } else {
        validateWhenVoucherIsNotPublic()
    }

    // public voucher region
    private fun validateWhenVoucherIsPublic(): Boolean {
        return if (voucherConfiguration.isPeriod) {
            isRecurringPublicVoucherValid()
        } else {
            isNonRecurringPublicVoucherValid()
        }
    }

    private fun isRecurringPublicVoucherValid(): Boolean {
        return !isVoucherNameError && !isStartDateError && !isEndDateError && validationDate.any { it.available }
    }

    private fun isNonRecurringPublicVoucherValid(): Boolean {
        return !isVoucherNameError && !isStartDateError && !isEndDateError
    }

    // private voucher region
    private fun validateWhenVoucherIsNotPublic(): Boolean {
        return if (voucherConfiguration.isPeriod) {
            isRecurringPrivateVoucherValid()
        } else {
            isNonRecurringPrivateVoucherValid()
        }
    }

    private fun isRecurringPrivateVoucherValid(): Boolean {
        return !isVoucherNameError && !isVoucherCodeError && !isStartDateError && !isEndDateError && validationDate.any { it.available }
    }

    private fun isNonRecurringPrivateVoucherValid(): Boolean {
        return !isVoucherNameError && !isVoucherCodeError && !isStartDateError && !isEndDateError
    }
}
