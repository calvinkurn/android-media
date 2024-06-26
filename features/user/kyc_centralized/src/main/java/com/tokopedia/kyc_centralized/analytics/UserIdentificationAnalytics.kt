package com.tokopedia.kyc_centralized.analytics

import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by alvinatin on 26/11/18.
 */

class UserIdentificationAnalytics private constructor(private val projectID: Int, private val kycFlowType: String) {

    private object Event {
        const val CLICK_ACCOUNT = "clickAccount"
        const val VIEW_KYC = "viewKYC"
        const val VIEW_ACCOUNT_IRIS = "viewAccountIris"
        const val CLICK_KYC = "clickKYC"
        const val VIEW_TRADEIN = "viewTradeIn"
        const val CLICK_TRADEIN = "clickTradeIn"
        const val CLICK_VERIFICATION = "clickVerification"
    }

    private object Action {
        const val CLICK_ON_BUTTON_BACK = "click on button back"
        const val VIEW_KYC_ONBOARDING = "view on KYC onboarding"
        const val CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding"
        const val CLICK_ON_MULAI_ONBOARDING = "click on button mulai"
        const val VIEW_PENDING_PAGE = "view on menunggu verifikasi"

        const val CLICK_ON_TNC_KYC = "click on tnc kyc"

        const val CLICK_ON_MENGERTI_PENDING_PAGE = "click on button mengerti"
        const val VIEW_SUCCESS_SNACKBAR_PENDING_PAGE = "view on success message verifikasi"
        const val VIEW_REJECTED_PAGE = "view on gagal verifikasi"

        const val CLICK_NEXT_REJECTED_PAGE = "click on button upload ulang"
        const val CLICK_ON_KEMBALI_BLACKLIST_PAGE = "click on button kembali"
        const val VIEW_SUCCES_PAGE = "view on success terverifikasi"
        const val CLICK_BACK_SUCCESS_PAGE = "click on back success terverfikasi"
        const val CLICK_RETRY_VERIFICATION = "click on button verifikasi ulang"
        const val CLICK_TERMS_AND_CONDITION_SUCCESS_PAGE = "click on syarat dan ketentuan sukses terverfikasi"
    }

    private object Category {
        const val KYC_PAGE = "kyc page"
        const val KYC_ONBOARDING_PAGE = "kyc onboarding page"
        const val KYC_PAGE_TRADEIN = "kyc trade in page"
    }

    private object Label {
        const val labelOne = "1"
        const val labelTwo = "2"
        const val labelThree = "3"
        const val labelFour = "4"

        const val LABEL_CHECK = "check"
        const val LABEL_UNCHECK = "uncheck"
    }

    private object ScreenName {
        const val PENDING_VERIFICATION = "menunggu verifikasi kyc page"
        const val SUCCESS_VERIFICATION = "success message verifikasi kyc page"
        const val SUCCESS_VERIFIED = "success terverifikasi kyc page"
    }

    fun eventViewOnKYCOnBoarding() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_KYC_ONBOARDING,
            "success - $projectID - $kycFlowType"
        ), "35165")
    }

    fun eventClickOnBackOnBoarding() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelOne} - click - $projectID - $kycFlowType"
        ), "2619")
    }

    fun eventClickKycTnc(isChecked: Boolean) {
        track(TrackAppUtils.gtmData(
            Event.CLICK_VERIFICATION,
            Category.KYC_PAGE,
            Action.CLICK_ON_TNC_KYC,
            if(isChecked) "${Label.LABEL_CHECK} - $projectID - $kycFlowType"
            else "${Label.LABEL_UNCHECK} - $projectID - $kycFlowType"
        ), "")
    }

    fun eventClickOnNextOnBoarding() {
        if (projectID == PROJECT_ID_TRADE_IN) {
            sendTradeInClickEvent(Action.CLICK_NEXT_ONBOARDING, "")
        }

        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_MULAI_ONBOARDING,
                "click - $projectID - $kycFlowType"
        ), "2618")
    }

    fun eventViewPendingPage() {
        sendScreenName("${ScreenName.PENDING_VERIFICATION} / $projectID - $kycFlowType")
    }

    fun eventClickBackPendingPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelTwo} - $projectID - $kycFlowType"
        ), "2619")
    }

    fun eventClickOnButtonPendingPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_MENGERTI_PENDING_PAGE,
                "click - $projectID - $kycFlowType"
        ), "2664")
    }

    fun eventViewSuccessSnackbarPendingPage() {
        sendScreenName("${ScreenName.SUCCESS_VERIFICATION} / $projectID - $kycFlowType")
    }

    fun eventViewRejectedPage() {
        track(TrackAppUtils.gtmData(
                Event.VIEW_ACCOUNT_IRIS,
                Category.KYC_PAGE,
                Action.VIEW_REJECTED_PAGE,
            "success - $projectID - $kycFlowType"
        ), "35137")
    }

    fun eventClickBackRejectedPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelThree} - $projectID - $kycFlowType"
        ), "2619")
    }

    fun eventClickNextRejectedPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_NEXT_REJECTED_PAGE,
                "click - $projectID - $kycFlowType"
        ), "2666")
    }

    fun eventClickOnButtonBlacklistPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_KEMBALI_BLACKLIST_PAGE,
                "click - $projectID - $kycFlowType"
        ), "2668")
    }

    fun eventClickBackBlacklistPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                "${Label.labelFour} - $projectID - $kycFlowType"
        ), "2619")
    }

    fun eventViewSuccessPage() {
        sendScreenName("${ScreenName.SUCCESS_VERIFIED} - $projectID - $kycFlowType")
    }

    fun eventClickBackSuccessPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_BACK_SUCCESS_PAGE,
            "success - $projectID - $kycFlowType"
        ), "35130")
    }

    fun eventClickTermsSuccessPage() {
        track(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_PAGE,
                Action.CLICK_TERMS_AND_CONDITION_SUCCESS_PAGE,
            "click - $projectID - $kycFlowType"
        ), "35136")
    }

    private fun sendTradeInClickEvent(Action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_TRADEIN,
                Category.KYC_PAGE_TRADEIN,
                Action,
                label
        ))
    }

    private fun track(data: MutableMap<String, Any>, trackerId: String) {
        data[KYCConstant.BUSINESS_UNIT] = KYCConstant.USER_PLATFORM
        data[KYCConstant.CURRENT_SITE] = KYCConstant.TOKOPEDIA_MARKETPLACE
        data[KYCConstant.TRACKER_ID] = trackerId
        TrackApp.getInstance().gtm.sendGeneralEvent(data)

    }

    private fun sendScreenName(screen: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screen)
    }

    companion object {
        @JvmStatic
        fun createInstance(projectID: Int, kycType: String): UserIdentificationAnalytics {
            return UserIdentificationAnalytics(projectID, kycType)
        }

        private const val TYPE_ALA_CARTE = "ala carte"
        private const val TYPE_CKYC = "ckyc"

        private const val PROJECT_ID_TRADE_IN = 4
    }
}
