package com.tokopedia.pms.analytics

sealed class PmsEvents {
    data class DeferredPaymentsShownEvent(val waitingPaymentCount: Int) : PmsEvents()
    data class ChevronTapClickEvent(val tag: Int) : PmsEvents()
    data class InvokeCancelTransactionBottomSheetEvent(val tag: Int) : PmsEvents()
    data class ConfirmCancelTransactionEvent(val tag: Int) : PmsEvents()
    data class InvokeCancelTransactionOnDetailEvent(val tag: Int) : PmsEvents()
    data class ShowTransactionDetailEvent(val tag: Int) : PmsEvents()

    data class InvokeEditBcaUserIdEvent(val tag: Int) : PmsEvents()
    data class ConfirmEditBcaUserIdEvent(val tag: Int) : PmsEvents()

    data class InvokeChangeAccountDetailsEvent(val tag: Int) : PmsEvents()
    data class ConfirmAccountDetailsEvent(val tag: Int) : PmsEvents()

    data class UploadPaymentProofEvent(val tag: Int) : PmsEvents()
    data class SelectImageEvent(val tag: Int) : PmsEvents()
    data class SelectAnotherImageEvent(val tag: Int) : PmsEvents()
    data class ConfirmSelectedImageEvent(val tag: Int) : PmsEvents()

    data class WaitingCardClickEvent(val paymentType: String) : PmsEvents()
    data class HowToPayRedirectionEvent(val tag: Int) : PmsEvents()
}