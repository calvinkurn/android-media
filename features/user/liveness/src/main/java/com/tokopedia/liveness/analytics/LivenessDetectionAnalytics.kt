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

    fun eventViewFaceInCenter() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_FACE_CENTER,
                ""
        ))
    }

    fun eventClickBackFaceInCenter() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo
        ))
    }

    fun eventViewCloserFaceToScreen() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_FACE_CLOSER_SCREEN,
                ""
        ))
    }

    fun eventClickBackCloserFaceToScreen() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree
        ))
    }

    fun eventViewMultipleFaces() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_FACE_MULTIPLE,
                ""
        ))
    }

    fun eventClickBackMultipleFaces() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelFour
        ))
    }

    fun eventViewBlinkDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_BLINK_DETECTION,
                ""
        ))
    }

    fun eventSuccessdBlinkDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.SUCCESS_BLINK_DETECTION,
                ""
        ))
    }

    fun eventClickBackBlinkDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelFive
        ))
    }

    fun eventViewMouthDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_MOUTH_DETECTION,
                ""
        ))
    }

    fun eventSuccessdMouthDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.SUCCESS_MOUTH_DETECTION,
                ""
        ))
    }

    fun eventClickBackMouthDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelSix
        ))
    }

    fun eventViewHeadDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.VIEW_HEAD_DETECTION,
                ""
        ))
    }

    fun eventSuccessdHeadDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.SUCCESS_HEAD_DETECTION,
                ""
        ))
    }

    fun eventClickBackHeadDetection() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelSeven
        ))
    }

    fun eventClickTimeout() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                Label.labelTimeout
        ))
    }

    fun eventClickBackTimeout() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTimeout
        ))
    }

    fun eventClickConnectionTimeout() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_COBA_LAGI,
                Label.labelConnectionTimeout
        ))
    }

    fun eventClickBackConnectionTimeout() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_LIVENESS_FAILED_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelConnectionTimeout
        ))
    }
}