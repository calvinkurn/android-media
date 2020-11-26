package com.tokopedia.kyc_centralized.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

/**
 * @author by alvinatin on 26/11/18.
 */
class UserIdentificationAnalytics private constructor(private val projectID: Int) {

    private object Event {
        const val CLICK_ACCOUNT = "clickAccount"
        const val VIEW_KYC = "viewKYC"
        const val CLICK_KYC = "clickKYC"
        const val VIEW_TRADEIN = "viewTradeIn"
        const val CLICK_TRADEIN = "clickTradeIn"
    }

    private object Action {
        const val CLICK_ON_BUTTON_BACK = "click on button back"
        const val VIEW_KYC_ONBOARDING = "view on KYC onboarding"
        const val CLICK_NEXT_ONBOARDING = "click on lanjut kyc onboarding"
        const val CLICK_ON_MULAI_ONBOARDING = "click on button mulai"
        const val VIEW_PENDING_PAGE = "view on menunggu verifikasi"

        const val CLICK_ON_MENGERTI_PENDING_PAGE = "click on button mengerti"
        const val VIEW_SUCCESS_SNACKBAR_PENDING_PAGE = "view on success message verifikasi"
        const val VIEW_REJECTED_PAGE = "view on gagal verifikasi"

        const val CLICK_NEXT_REJECTED_PAGE = "click on button upload ulang"
        const val CLICK_ON_KEMBALI_BLACKLIST_PAGE = "click on button kembali"
        const val VIEW_SUCCES_PAGE = "view on success terverifikasi"
        const val CLICK_BACK_SUCCESS_PAGE = "click on back success terverfikasi"
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
    }

    fun eventViewOnKYCOnBoarding() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_KYC_ONBOARDING,
                ""
        ))
    }

    fun eventClickOnBackOnBoarding() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelOne
        ))
    }

    fun eventClickOnNextOnBoarding() {
        if (projectID == 4) {
            sendTradeInClickEvent(Action.CLICK_NEXT_ONBOARDING, "")
        } else {
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                    Event.CLICK_ACCOUNT,
                    Category.KYC_ONBOARDING_PAGE,
                    Action.CLICK_ON_MULAI_ONBOARDING,
                    ""
            ))
        }
    }

    fun eventViewPendingPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_PENDING_PAGE,
                ""
        ))
    }

    fun eventClickBackPendingPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelTwo
        ))
    }

    fun eventClickOnButtonPendingPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_MENGERTI_PENDING_PAGE,
                ""
        ))
    }

    fun eventViewSuccessSnackbarPendingPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SUCCESS_SNACKBAR_PENDING_PAGE,
                ""
        ))
    }

    fun eventViewRejectedPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_REJECTED_PAGE,
                ""
        ))
    }

    fun eventClickBackRejectedPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelThree
        ))
    }

    fun eventClickNextRejectedPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_NEXT_REJECTED_PAGE,
                ""
        ))
    }

    fun eventClickOnButtonBlacklistPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_KEMBALI_BLACKLIST_PAGE,
                ""
        ))
    }

    fun eventClickBackBlacklistPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_ACCOUNT,
                Category.KYC_ONBOARDING_PAGE,
                Action.CLICK_ON_BUTTON_BACK,
                Label.labelFour
        ))
    }

    fun eventViewSuccessPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.VIEW_KYC,
                Category.KYC_PAGE,
                Action.VIEW_SUCCES_PAGE,
                ""
        ))
    }

    fun eventClickBackSuccessPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_BACK_SUCCESS_PAGE,
                ""
        ))
    }

    fun eventClickTermsSuccessPage() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_KYC,
                Category.KYC_PAGE,
                Action.CLICK_TERMS_AND_CONDITION_SUCCESS_PAGE,
                ""
        ))
    }

    private fun sendTradeInClickEvent(Action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                Event.CLICK_TRADEIN,
                Category.KYC_PAGE_TRADEIN,
                Action,
                label
        ))
    }

    companion object {
        @JvmStatic
        fun createInstance(projectID: Int): UserIdentificationAnalytics {
            return UserIdentificationAnalytics(projectID)
        }
    }

}