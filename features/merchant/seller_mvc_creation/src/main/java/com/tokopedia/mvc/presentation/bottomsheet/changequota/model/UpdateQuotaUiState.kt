package com.tokopedia.mvc.presentation.bottomsheet.changequota.model

data class UpdateQuotaUiState(
    val isValidInput: Boolean = true,
    val isSelectedOptions : Boolean = true,
    val estimationSpending : Long = 0,
    val quotaReq : Long = 0,
    val isInputOnUnderMinimumReq : Boolean = false
)

sealed class UpdateQuotaEffect{
    data class SuccessToUpdate(val nameVoucher : String, val isApplyToAllPeriodCoupon : Boolean) : UpdateQuotaEffect()
    data class FailToUpdate(val nameVoucher : String, val throwable: Throwable) : UpdateQuotaEffect()
    data class SuccessToGetDetailVoucher(val updateQuotaModel: UpdateQuotaModel) : UpdateQuotaEffect()
    data class FailToGetDetailVoucher(val throwable: Throwable) : UpdateQuotaEffect()
}
