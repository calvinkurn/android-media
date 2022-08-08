package com.tokopedia.liveness.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

class LivenessDetectionAnalytics @Inject constructor() {

    private object Event {
        const val CLICK_ACCOUNT = "clickAccount"
    }

    private object Action {
        const val CLICK_ON_BUTTON_BACK = "click on button back"

        const val VIEW_FACE_CENTER = "wajah tepat di tengah"
        const val VIEW_FACE_CLOSER_SCREEN = "wajah dekat ke layar"
        const val VIEW_FACE_MULTIPLE = "wajah lebih dari satu"

        const val VIEW_BLINK_DETECTION = "blinking detection"
        const val SUCCESS_BLINK_DETECTION = "succeed blinking detection"

        const val VIEW_MOUTH_DETECTION = "mouth detection"
        const val SUCCESS_MOUTH_DETECTION = "succeed mouth detection"

        const val VIEW_HEAD_DETECTION = "head detection"
        const val SUCCESS_HEAD_DETECTION = "succeed head detection"

        const val CLICK_ON_BUTTON_COBA_LAGI = "click on button coba lagi"

    }

    private object Category {
        const val KYC_LIVENESS_PAGE = "kyc liveness page"
        const val KYC_LIVENESS_FAILED_PAGE = "kyc liveness failed page"
    }

    private object Label {
        const val labelTwo = "2"
        const val labelThree = "3"
        const val labelFour = "4"
        const val labelFive = "5"
        const val labelSix= "6"
        const val labelSeven= "7"
        const val labelTimeout= "timeout"
        const val labelConnectionTimeout= "connection timeout"
    }


    fun eventViewFaceInCenter(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_FACE_CENTER,
                getLabel(projectID, isSuccess, message)
        ), "2630")
    }

    fun eventClickBackFaceInCenter(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelTwo} $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventViewCloserFaceToScreen(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_FACE_CLOSER_SCREEN,
                getLabel(projectID, isSuccess, message)
        ), "2632")
    }

    fun eventClickBackCloserFaceToScreen(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelThree} $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventViewMultipleFaces(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_FACE_MULTIPLE,
                getLabel(projectID, isSuccess, message)
        ), "2634")
    }

    fun eventClickBackMultipleFaces(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelFour} $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventViewBlinkDetection(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_BLINK_DETECTION,
                getLabel(projectID, isSuccess, message)
        ), "2636")
    }

    fun eventSuccessBlinkDetection(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.SUCCESS_BLINK_DETECTION,
                getLabel(projectID, isSuccess, message)
        ), "2637")
    }

    fun eventClickBackBlinkDetection(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelFive} $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventViewMouthDetection(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_MOUTH_DETECTION,
                getLabel(projectID, isSuccess, message)
        ), "2639")
    }

    fun eventSuccessMouthDetection(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.SUCCESS_MOUTH_DETECTION,
                getLabel(projectID, isSuccess, message)
        ), "2640")
    }

    fun eventClickBackMouthDetection(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelSix} $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventViewHeadDetection(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_HEAD_DETECTION,
                getLabel(projectID, isSuccess, message)
        ), "2642")
    }

    fun eventSuccessHeadDetection(projectID: String, isSuccess: Boolean, message: String = "") {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.SUCCESS_HEAD_DETECTION,
                getLabel(projectID, isSuccess, message)
        ), "2643")
    }

    fun eventClickBackHeadDetection(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelSeven} $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventClickTimeout(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                "${Label.labelTimeout} - $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventClickBackTimeout(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelTimeout} - $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventClickConnectionTimeout(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                "${Label.labelConnectionTimeout} - $projectID - ${getKycType(projectID)}"
        ))
    }

    fun eventClickBackConnectionTimeout(projectID: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelConnectionTimeout} - $projectID - ${getKycType(projectID)}"
        ))
    }

    private fun getLabel(projectID: String, isSuccess: Boolean, message: String = ""): String {
        return if (isSuccess) {
            "success - $projectID - ${getKycType(projectID)}"
        } else {
            "fail - $message - $projectID - ${getKycType(projectID)}"
        }
    }

    private fun getKycType(projectID: String): String {
        val projectIdAlaCarte = listOf(
            PROJECT_ID_HOME_CREDIT,
            PROJECT_ID_CO_BRAND_CC,
            PROJECT_ID_GO_CICIL
        )
        return if (projectIdAlaCarte.contains(projectID)) {
            TYPE_ALA_CARTE
        } else {
            TYPE_CKYC
        }
    }

    private fun track(data: MutableMap<String, Any>, trackerId: String) {
        data[BUSINESS_UNIT] = USER_PLATFORM
        data[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        data[TRACKER_ID] = trackerId
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    companion object {
        private const val PROJECT_ID_HOME_CREDIT = "16"
        private const val PROJECT_ID_CO_BRAND_CC = "18"
        private const val PROJECT_ID_GO_CICIL = "21"
        private const val TYPE_ALA_CARTE = "ala carte"
        private const val TYPE_CKYC = "ckyc"

        const val BUSINESS_UNIT = "businessUnit"
        const val USER_PLATFORM = "user platform"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val CURRENT_SITE = "currentSite"
        const val TRACKER_ID = "trackerId"
    }
}