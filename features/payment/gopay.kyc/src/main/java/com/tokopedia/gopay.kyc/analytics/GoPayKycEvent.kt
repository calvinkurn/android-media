package com.tokopedia.gopay.kyc.analytics

sealed class GoPayKycEvent {
    sealed class Impression {
        data class OpenScreenEvent(val pageSource: String): GoPayKycEvent()
        data class KycFailedImpression(val pageSource: String): GoPayKycEvent()
    }
    sealed class Click {
        data class BackPressEvent(val pageSource: String): GoPayKycEvent()
        data class UpgradeKycEvent(val pageSource: String): GoPayKycEvent()
        data class TakePhotoEvent(val eventLabel: String, val pageSource: String): GoPayKycEvent()
        data class ReTakePhotoEvent(val eventLabel: String, val pageSource: String): GoPayKycEvent()
        data class ConfirmPhotoEvent(val eventLabel: String, val pageSource: String): GoPayKycEvent()
        data class ConfirmOkDialogEvent(val eventLabel: String, val pageSource: String): GoPayKycEvent()
        data class ExitKycDialogEvent(val eventLabel: String, val pageSource: String): GoPayKycEvent()
        data class UploadKycEvent(val eventLabel: String): GoPayKycEvent()
        data class TncEvent(val eventLabel: String): GoPayKycEvent()
        data class SubmitOkEvent(val eventLabel: String): GoPayKycEvent()
        data class RetrySubmitEvent(val eventLabel: String): GoPayKycEvent()
    }

}