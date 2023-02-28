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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelTwo} $projectID - ${getKycType(projectID)}"
        ), "2629")
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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelThree} $projectID - ${getKycType(projectID)}"
        ), "2629")
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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelFour} $projectID - ${getKycType(projectID)}"
        ), "2629")
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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelFive} $projectID - ${getKycType(projectID)}"
        ), "2629")
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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelSix} $projectID - ${getKycType(projectID)}"
        ), "2629")
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
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelSeven} $projectID - ${getKycType(projectID)}"
        ), "2629")
    }

    fun eventClickTimeout(projectID: String) {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                "${Label.labelTimeout} - $projectID - ${getKycType(projectID)}"
        ), "2656")
    }

    fun eventClickConnectionTimeout(projectID: String) {
        track(TrackAppUtils.gtmData(
            Event.CLICK_ACCOUNT,
            Category.KYC_LIVENESS_FAILED_PAGE,
            Action.CLICK_ON_BUTTON_COBA_LAGI,
            "${Label.labelConnectionTimeout} - $projectID - ${getKycType(projectID)}"
        ), "2656")
    }

    fun eventClickBackTimeout(projectID: String) {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelTimeout} - $projectID - ${getKycType(projectID)}"
        ), "2657")
    }

    fun eventClickBackConnectionTimeout(projectID: String) {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelConnectionTimeout} - $projectID - ${getKycType(projectID)}"
        ), "2657")
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
            PROJECT_ID_GO_CICIL,
            OFFICIAL_STORE_PROJECT_ID
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
        private const val OFFICIAL_STORE_PROJECT_ID = "23"
        private const val TYPE_ALA_CARTE = "ala carte"
        private const val TYPE_CKYC = "ckyc"

        const val BUSINESS_UNIT = "businessUnit"
        const val USER_PLATFORM = "user platform"
        const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val CURRENT_SITE = "currentSite"
        const val TRACKER_ID = "trackerId"
    }
}
