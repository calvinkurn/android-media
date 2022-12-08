package com.tokopedia.mvc.presentation.creation.step2.uimodel

import com.tokopedia.mvc.domain.entity.VoucherConfiguration
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
    val error: Throwable? = null
) {
    fun isInputValid(): Boolean = !isVoucherNameError
}
