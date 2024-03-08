package com.tokopedia.kyc_centralized.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class UserIdentificationCommonAnalytics private constructor(
    private val projectID: Int,
    private val kycFlowType: String
) {
    private object Event {
        const val CLICK_ACCOUNT = "clickAccount"
        const val VIEW_KYC = "viewKYC"
        const val VIEW_ACCOUNT_IRIS = "viewAccountIris"
        const val CLICK_KYC = "clickKYC"
        const val VIEW_TRADEIN = "viewTradeIn"
        const val CLICK_TRADEIN = "clickTradeIn"
    }

    private object Action {
        const val CLICK_ON_BUTTON_BACK = "click on button back"
        const val VIEW_KYC_ONBOARDING = "view on KYC onboarding"
        const val CLICK_BACK_ONBOARDING = "click on back KYC onboarding"
        const val CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding"
        const val VIEW_KTP_PAGE = "view on panduan KTP KYC"
        const val CLICK_BACK_KTP_PAGE = "click on back panduan KTP KYC"
        const val CLICK_NEXT_KTP_PAGE = "click on ambil foto KTP"
        const val CLICK_ON_BUTTON_AMBIL_KTP_PAGE = "click on button ambil foto ktp"
        const val VIEW_ERROR_IMAGE_TOO_LARGE_KTP =
            "view error message foto melebihi ukuran maksimum"
        const val VIEW_OPEN_CAMERA_KTP = "view on open camera KTP"
        const val CLICK_BACK_CAMERA_KTP = "click on close ambil foto KTP"
        const val CLICK_SHUTTER_CAMERA_KTP = "click on foto KTP"
        const val CLICK_ON_BUTTON_CAPTURE_CAMERA = "click on button capture"
        const val CLICK_FLIP_CAMERA_KTP = "click on change camera foto KTP"
        const val VIEW_IMAGE_PREVIEW_KTP = "view on take foto KTP"
        const val CLICK_CLOSE_IMAGE_PREVIEW_KTP = "click on close take foto KTP"
        const val CLICK_RECAPTURE_KTP = "click on foto ulang KTP"
        const val CLICK_BUTTON_RECAPTURE = "click on button foto ulang"
        const val CLICK_NEXT_IMAGE_PREVIEW_KTP = "click on lanjut take foto KTP"
        const val CLICK_ON_BUTTON_LANJUT_PREVIEW = "click on button lanjut"
        const val VIEW_SELFIE_PAGE = "view on panduan selfie KTP"
        const val CLICK_NEXT_SELFIE_PAGE = "click on ambil foto diri bersama KTP"
        const val CLICK_ON_BUTTON_VERIFIKASI_WAJAH_PAGE =
            "click on button mulai verifikasi wajah"
        const val CLICK_BACK_SELFIE_PAGE = "click on back panduan selfie KTP"
        const val VIEW_ERROR_IMAGE_TOO_LARGE_SELFIE =
            "view on error maximum foto size selfie KTP"
        const val VIEW_OPEN_CAMERA_SELFIE = "view on open camera selfie bersama KTP"
        const val CLICK_BACK_CAMERA_SELFIE = "click on back selfie bersama KTP"
        const val CLICK_SHUTTER_CAMERA_SELFIE = "click on foto selfie bersama KTP"
        const val CLICK_ON_BUTTON_SHUTTER_CAMERA_SELFIE =
            "click on button ambil foto selfie"
        const val CLICK_FLIP_CAMERA_SELFIE = "click on change camera selfie bersama KTP"
        const val VIEW_IMAGE_PREVIEW_SELFIE = "view on take foto selfie bersama KTP"
        const val CLICK_CLOSE_IMAGE_PREVIEW_SELFIE = "click on back take selfie KTP"
        const val CLICK_RECAPTURE_SELFIE = "click on foto ulang take selfie KTP"
        const val CLICK_NEXT_IMAGE_PREVIEW_SELFIE = "click on lanjut take selfie KTP"
        const val VIEW_FINAL_FORM_PAGE = "view on preview KTP and selfie"
        const val CLICK_BACK_FINAL_FORM_PAGE = "click on back preview KTP and selfie"
        const val CLICK_CHANGE_KTP_FINAL_FORM_PAGE = "click on ubah foto KTP"
        const val CLICK_RETAKE_KTP_FINAL_FORM_PAGE = "click on button foto ulang ktp"
        const val CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE = "click on ubah foto selfie KTP"
        const val CLICK_RETAKE_SELFIE_FINAL_FORM_PAGE =
            "click on button verifikasi ulang wajah"
        const val CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE =
            "click on syarat dan ketentuan sukses terverifikasi"
        const val CLICK_UPLOAD_PHOTOS = "click on unggah foto KTP"
        const val CLICK_RETAKE_KTP_SELFIE_FINAL_FORM_PAGE = "click on button coba lagi"
        const val CLICK_ON_BUTTON_EXIT = "click on button keluar"
        const val CLICK_ON_BUTTON_STAY = "click on button lanjut kirim"
        const val CLICK_ON_BUTTON_COBA_LAGI = "click on button coba lagi"
    }

    private object Category {
        const val KYC_PAGE = "kyc page"
        const val KYC_KTP_PAGE = "kyc ktp page"
        const val KYC_SELFIE_PAGE = "kyc ktp page"
        const val KYC_LIVENESS_PAGE = "kyc liveness page"
        const val KYC_SUBMISSION_PAGE = "kyc submission page"
        const val KYC_PAGE_TRADEIN = "kyc trade in page"
        const val KYC_LIVENESS_FAILED_PAGE = "kyc liveness failed page"
    }

    private object Label {
        const val labelOne = "1"
        const val labelTwo = "2"
        const val labelThree = "3"
        const val labelConnectionTimeout = "connection timeout"
    }

    fun eventClickBackSelfiePage(isLiveness: Boolean) {
        if (isLiveness) {
            track(
                TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_LIVENESS_PAGE,
                    Action.CLICK_ON_BUTTON_BACK,
                    Label.labelOne + " - " + projectID + " - " + kycFlowType
                ), "2629"
            )
        } else {
            track(
                TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_SELFIE_PAGE,
                    Action.CLICK_ON_BUTTON_BACK,
                    Label.labelOne + " - click - " + projectID + " - " + kycFlowType
                ), "2621"
            )
        }
    }

    fun eventViewSelfiePage(isSelfie: Boolean) {
        if (projectID == 4) {
            sendTradeInViewEvent(Action.VIEW_SELFIE_PAGE)
        }
        if (isSelfie) {
            track(
                TrackAppUtils.gtmData(
                    Event.VIEW_ACCOUNT_IRIS,
                    Category.KYC_PAGE,
                    Action.VIEW_SELFIE_PAGE,
                    "success - $projectID - $kycFlowType"
                ), "35141"
            )
        }
    }

    fun eventClickNextSelfiePage(isLiveness: Boolean) {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_NEXT_SELFIE_PAGE, "")
        }
        if (isLiveness) {
            track(
                TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_LIVENESS_PAGE,
                    Action.CLICK_ON_BUTTON_VERIFIKASI_WAJAH_PAGE,
                    "click - $projectID - $kycFlowType"
                ), "2628"
            )
        } else {
            track(
                TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_SELFIE_PAGE,
                    Action.CLICK_NEXT_SELFIE_PAGE,
                    "click - $projectID - $kycFlowType"
                ), ""
            )
        }
    }

    fun eventViewOpenCameraKtp() {
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_KTP,
                "success - $projectID - $kycFlowType"
            ), "35138"
        )
    }

    fun eventViewOpenCameraSelfie() {
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_OPEN_CAMERA_SELFIE,
                "success - $projectID - $kycFlowType"
            ), "35139"
        )
    }

    fun eventClickBackCameraKtp() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo + " - click - " + projectID + " - " + kycFlowType
            ), "2621"
        )
    }

    fun eventClickBackCameraSelfie() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_BACK_CAMERA_SELFIE,
                "click - $projectID - $kycFlowType"
            ), "35240"
        )
    }

    fun eventClickFlipCameraKtp() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_FLIP_CAMERA_KTP, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_FLIP_CAMERA_KTP,
                "success - $projectID - $kycFlowType"
            ), "35131"
        )
    }

    fun eventClickShutterCameraKtp() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_SHUTTER_CAMERA_KTP, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_CAPTURE_CAMERA,
                "click - $projectID - $kycFlowType"
            ), "2622"
        )
    }

    fun eventClickShutterCameraSelfie() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_SHUTTER_CAMERA_SELFIE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_SHUTTER_CAMERA_SELFIE,
                "success - $projectID - $kycFlowType"
            ), "35133"
        )
    }

    fun eventViewErrorImageTooLargeKtpPage() {
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_KTP_PAGE,
                Action.VIEW_ERROR_IMAGE_TOO_LARGE_KTP,
                "failed  - " + Action.VIEW_ERROR_IMAGE_TOO_LARGE_KTP + " - " + projectID + " - " + kycFlowType
            ), "2627"
        )
    }

    fun eventClickFlipCameraSelfie() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_FLIP_CAMERA_SELFIE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_FLIP_CAMERA_SELFIE,
                "success - $projectID - $kycFlowType"
            ), "35132"
        )
    }

    fun eventViewImagePreviewKtp() {
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_KTP,
                "success - $projectID - $kycFlowType"
            ), "35215"
        )
    }

    fun eventViewImagePreviewSelfie() {
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_IMAGE_PREVIEW_SELFIE,
                "success - $projectID - $kycFlowType"
            ), "35216"
        )
    }

    fun eventClickCloseImagePreviewKtp() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree + " - click - " + projectID + " - " + kycFlowType
            ), "2646"
        )
    }

    fun eventClickCloseImagePreviewSelfie() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_CLOSE_IMAGE_PREVIEW_SELFIE,
                "click - $projectID - $kycFlowType"
            ), "35241"
        )
    }

    fun eventClickRecaptureKtp() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_RECAPTURE_KTP, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_BUTTON_RECAPTURE,
                "click - $projectID - $kycFlowType"
            ), "2625"
        )
    }

    fun eventClickRecaptureSelfie() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_RECAPTURE_SELFIE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_RECAPTURE_SELFIE,
                "success - $projectID - $kycFlowType"
            ), "35134"
        )
    }

    fun eventClickNextImagePreviewKtp() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_NEXT_IMAGE_PREVIEW_KTP, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_LANJUT_PREVIEW,
                "click - $projectID - $kycFlowType"
            ), "2626"
        )
    }

    fun eventClickNextImagePreviewSelfie() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_NEXT_IMAGE_PREVIEW_SELFIE,
                "success - $projectID - $kycFlowType"
            ), "35135"
        )
    }

    fun eventViewFinalForm() {
        if (projectID == 4) {
            sendTradeInViewEvent(Action.VIEW_FINAL_FORM_PAGE)
        }
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_FINAL_FORM_PAGE,
                "success - $projectID - $kycFlowType"
            ), "35142"
        )
    }

    fun eventClickChangeKtpFinalFormPage() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_CHANGE_KTP_FINAL_FORM_PAGE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_KTP_FINAL_FORM_PAGE,
                "click - $projectID - $kycFlowType"
            ), "2645"
        )
    }

    fun eventClickBackChangeKtpFinalFormPage() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne + " - click - " + projectID + " - " + kycFlowType
            ), "2646"
        )
    }

    fun eventClickBackChangeSelfieFinalFormPage() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo + " - click - " + projectID + " - " + kycFlowType
            ), "2646"
        )
    }

    fun eventClickChangeSelfieFinalFormPage() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_CHANGE_SELFIE_FINAL_FORM_PAGE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_SELFIE_FINAL_FORM_PAGE,
                "click - $projectID - $kycFlowType"
            ), "2647"
        )
    }

    fun eventClickChangeKtpSelfieFinalFormPage() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_RETAKE_KTP_SELFIE_FINAL_FORM_PAGE,
                "click - $projectID - $kycFlowType"
            ), "2649"
        )
    }

    fun eventClickBackChangeKtpSelfieFinalFormPage() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree + " - click - " + projectID + " - " + kycFlowType
            ), "2646"
        )
    }

    fun eventClickTermsFinalFormPage() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_TERMS_AND_CONDITION_FINAL_FORM_PAGE,
                "click - $projectID - $kycFlowType"
            ), "35136"
        )
    }

    fun eventClickUploadPhotosTradeIn(label: String) {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_UPLOAD_PHOTOS, label)
        }
    }

    fun eventClickBackFinalForm() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_BACK_FINAL_FORM_PAGE,
                "success - $projectID - $kycFlowType"
            ), "35129"
        )
    }

    fun eventViewKtpPage() {
        if (projectID == 4) {
            sendTradeInViewEvent(Action.VIEW_KTP_PAGE)
        }
        track(
            TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_KTP_PAGE,
                "success - $projectID - $kycFlowType"
            ), "35140"
        )
    }

    fun eventClickNextKtpPage() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_NEXT_KTP_PAGE, "")
        }
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_AMBIL_KTP_PAGE,
                "click - $projectID - $kycFlowType"
            ), "2620"
        )
    }

    fun eventClickBackKtpPage() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_KTP_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne + " - click - " + projectID + " - " + kycFlowType
            ), "2621"
        )
    }

    fun eventClickDialogExit() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_EXIT,
                "click - $projectID - $kycFlowType"
            ), "2654"
        )
    }

    fun eventClickDialogStay() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_SUBMISSION_PAGE,
                Action.CLICK_ON_BUTTON_STAY,
                "click - $projectID - $kycFlowType"
            ), "2655"
        )
    }

    private fun sendTradeInClickEvent(Action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.CLICK_TRADEIN,
                Category.KYC_PAGE_TRADEIN,
                Action,
                label
            )
        )
    }

    private fun sendTradeInViewEvent(Action: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            TrackAppUtils.gtmData(
                Event.VIEW_TRADEIN,
                Category.KYC_PAGE_TRADEIN,
                Action,
                ""
            )
        )
    }

    fun eventClickConnectionTimeout() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                Label.labelConnectionTimeout + " - " + projectID + " - " + kycFlowType
            ), "2656"
        )
    }

    fun eventClickBackConnectionTimeout() {
        track(
            TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelConnectionTimeout + " - " + projectID + " - " + kycFlowType
            ), "2657"
        )
    }

    private fun track(data: MutableMap<String, Any>, trackerId: String) {
        data[BUSINESS_UNIT] = USER_PLATFORM
        data[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        data[TRACKER_ID] = trackerId
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {
        private const val BUSINESS_UNIT = "businessUnit"
        private const val USER_PLATFORM = "user platform"
        private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        private const val CURRENT_SITE = "currentSite"
        private const val TRACKER_ID = "trackerId"
        fun createInstance(projectID: Int, kycFlowType: String): UserIdentificationCommonAnalytics {
            return UserIdentificationCommonAnalytics(projectID, kycFlowType)
        }
    }
}
