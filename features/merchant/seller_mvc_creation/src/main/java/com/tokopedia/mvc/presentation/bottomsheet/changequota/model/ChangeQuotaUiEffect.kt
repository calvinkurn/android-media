package com.tokopedia.mvc.presentation.bottomsheet.changequota.model

data class ChangeQuotaUiEffect(
    val isValidInput: Boolean = true,
    val isSelectedOptions : Boolean = false,
    val estimationSpending : Long = 0,
    val quotaReq : Long = 0,
    val isInputOnUnderMinimumReq : Boolean = false
)

sealed class UpdateQuotaState{
    data class SuccessToUpdate(val nameVoucher : String, val isApplyToAllPeriodCoupon : Boolean) : UpdateQuotaState()
    data class FailToUpdate(val nameVoucher : String, val throwable: Throwable) : UpdateQuotaState()
    data class SuccessToGetDetailVoucher(val changeQuotaModel: ChangeQuotaModel) : UpdateQuotaState()
    data class FailToGetDetailVoucher(val throwable: Throwable) : UpdateQuotaState()
}
