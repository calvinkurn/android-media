package com.tokopedia.kyc_centralized.ui.gotoKyc.analytics

import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.track.builder.Tracker

object GotoKycAnalytics {

    private const val CATEGORY_ONBOARDING_PAGE= "goto kyc onboarding page"
    private const val CATEGORY_STATUS_PAGE =  "goto kyc status page"
    private const val CATEGORY_DOB_PAGE = "goto kyc dob page"
    private const val CATEGORY_DOB_PAGE_FAILED = "goto kyc failed dob page"
    private const val CATEGORY_ERROR_PAGE = "goto kyc error page"
    private const val CATEGORY_KTP_PAGE = "goto kyc ktp page"
    private const val CATEGORY_SELFIE_PAGE = "goto kyc selfie page"
    private const val CATEGORY_REVIEW_PAGE = "goto kyc review page"
    private const val CATEGORY_REVIEW_KTP_PAGE = "goto kyc review ktp page"
    private const val CATEGORY_REVIEW_SELFIE_PAGE = "goto kyc review selfie page"
    private const val ACTION_VIEW_KTP_PAGE = "view on kyc ktp page"
    private const val ACTION_VIEW_ONBOARDING_PAGE = "view kyc onboarding page"
    private const val ACTION_CLICK_BUTTON_AMBIL_KEUNTUNGAN = "click on button ambil keuntungannya"
    private const val ACTION_CLICK_BUTTON_BACK = "click on button back"
    private const val ACTION_CLICK_BUTTON_MULAI_VERIFIKASI = "click on button mulai verifikasi"
    private const val ACTION_CLICK_BUTTON_PAKAI_KTP = "click on button pakai ktp ini"
    private const val ACTION_CLICK_BUTTON_BACK_MANUAL_KTP = "click on button back - manual ktp"
    private const val ACTION_CLICK_BUTTON_BACK_MANUAL_SELFIE = "click on button back - manual selfie"
    private const val ACTION_CLICK_BUTTON_PAKAI_FOTO_INI = "click on button pakai foto ini"
    private const val ACTION_CLICK_CLOSE_BUTTON = "click on close button"
    private const val ACTION_CLICK_VERIFIKASI_ULANG = "click on button verifikasi ulang"
    private const val ACTION_CLICK_TOKOPEDIA_CARE = "click on button hubungi tokopedia care"
    private const val ACTION_CLICK_BUTTON_CONFIRMATION = "click on button konfirmasi"
    private const val ACTION_CLICK_BUTTON_CAPTURE = "click on button capture"
    private const val ACTION_CLICK_BUTTON_FOTO_ULANG = "click on button foto ulang"
    private const val ACTION_CLICK_ON_FOTO_MANUAL_CLOSE_BUTTON = "click on foto manual - close button"
    private const val ACTION_CLICK_ON_BUTTON_MANUAL = "click on button foto manual"
    private const val ACTION_CLICK_ON_BUTTON_OTOMATIS = "click on button foto otomatis"
    private const val ACTION_CLICK_ON_BUTTON_MULAI_SELFIE = "click on button mulai selfie"
    private const val ACTION_CLICK_ON_BUTTON_KIRIM_DOKUMEN = "click on button kirim dokumen"
    private const val ACTION_CLICK_ON_BACK_SELFIE_PAGE = "click on button back - selfie page"
    private const val ACTION_CLICK_RETAKE_KTP = "click on button retake ktp"
    private const val ACTION_CLICK_DELETE_KTP = "click on button delete ktp"
    private const val ACTION_CLICK_RETAKE_SELFIE = "click on button retake selfie"
    private const val ACTION_CLICK_DELETE_SELFIE = "click on button delete selfie"
    private const val ACTION_CLICK_KTP_IMAGE = "click on ktp image"
    private const val ACTION_CLICK_SELFIE_IMAGE = "click on selfie image"
    private const val ACTION_CLICK_KIRIM_ULANG = "click on button kirim ulang"
    private const val ACTION_CLICK_CLOSE_PENDING_PAGE = "click on close button gopay pending"
    private const val ACTION_CLICK_GET_READY_FIRST = "click on button siap siap dulu"
    private const val ACTION_VIEW_MANUAL_FOTO_QUESTION = "view on manual foto question"
    private const val ACTION_VIEW_STATUS_PAGE = "view on kyc status page"
    private const val ACTION_VIEW_DOB_PAGE = "view on kyc dob page"
    private const val ACTION_VIEW_DOB_PAGE_FAILED = "view on kyc failed dob page"
    private const val ACTION_VIEW_ERROR_PAGE = "view on error page"
    private const val ACTION_VIEW_GUIDE_SELFIE_PAGE = "view on persiapan selfie page"
    private const val ACTION_VIEW_SELFIE_PAGE = "view on selfie page"
    private const val ACTION_VIEW_REVIEW_PAGE = "view on kyc review page"
    private const val ACTION_VIEW_GOPAY_PENDING = "view on gopay status pending"
    private const val ACTION_SCAN_KTP_IMAGE = "scan ktp image"
    private const val ACTION_SCAN_SELFIE_IMAGE = "scan selfie image"
    private const val EVENT_CLICK_ACCOUNT = "clickAccount"
    private const val EVENT_VIEW_ACCOUNT_IRIS = "viewAccountIris"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val VALUE_TRACKER_ID_43322 = "43322"
    private const val VALUE_TRACKER_ID_43323 = "43323"
    private const val VALUE_TRACKER_ID_43324 = "43324"
    private const val VALUE_TRACKER_ID_43325 = "43325"
    private const val VALUE_TRACKER_ID_43326 = "43326"
    private const val VALUE_TRACKER_ID_43327 = "43327"
    private const val VALUE_TRACKER_ID_43328 = "43328"
    private const val VALUE_TRACKER_ID_43329 = "43329"
    private const val VALUE_TRACKER_ID_43330 = "43330"
    private const val VALUE_TRACKER_ID_43331 = "43331"
    private const val VALUE_TRACKER_ID_43333 = "43333"
    private const val VALUE_TRACKER_ID_43334 = "43334"
    private const val VALUE_TRACKER_ID_43335 = "43335"
    private const val VALUE_TRACKER_ID_43337 = "43337"
    private const val VALUE_TRACKER_ID_43338 = "43338"
    private const val VALUE_TRACKER_ID_43339 = "43339"
    private const val VALUE_TRACKER_ID_43340 = "43340"
    private const val VALUE_TRACKER_ID_43341 = "43341"
    private const val VALUE_TRACKER_ID_43343 = "43343"
    private const val VALUE_TRACKER_ID_43602 = "43602"
    private const val VALUE_TRACKER_ID_43603 = "43603"
    private const val VALUE_TRACKER_ID_43604 = "43604"
    private const val VALUE_TRACKER_ID_43605 = "43605"
    private const val VALUE_TRACKER_ID_43606 = "43606"
    private const val VALUE_TRACKER_ID_43607 = "43607"
    private const val VALUE_TRACKER_ID_43608 = "43608"
    private const val VALUE_TRACKER_ID_43609 = "43609"
    private const val VALUE_TRACKER_ID_43610 = "43610"
    private const val VALUE_TRACKER_ID_43611 = "43611"
    private const val VALUE_TRACKER_ID_43612 = "43612"
    private const val VALUE_TRACKER_ID_43613 = "43613"
    private const val VALUE_TRACKER_ID_43614 = "43614"
    private const val VALUE_TRACKER_ID_43615 = "43615"
    private const val VALUE_TRACKER_ID_43616 = "43616"
    private const val VALUE_TRACKER_ID_43623 = "43623"
    private const val VALUE_TRACKER_ID_43624 = "43624"
    private const val VALUE_TRACKER_ID_43625 = "43625"
    private const val VALUE_TRACKER_ID_43626 = "43626"
    private const val VALUE_TRACKER_ID_43627 = "43627"
    private const val VALUE_TRACKER_ID_43630 = "43630"
    private const val VALUE_TRACKER_ID_43631 = "43631"
    private const val VALUE_TRACKER_ID_43632 = "43632"
    private const val VALUE_TRACKER_ID_43633 = "43633"
    private const val VALUE_TRACKER_ID_43634 = "43634"
    private const val VALUE_TRACKER_ID_43635 = "43635"
    private const val VALUE_TRACKER_ID_43815 = "43815"
    private const val VALUE_TRACKER_ID_43816 = "43816"
    private const val VALUE_TRACKER_ID_43817 = "43817"
    private const val VALUE_TRACKER_ID_43888 = "43888"
    private const val VALUE_TRACKER_ID_43889 = "43889"
    private const val VALUE_TRACKER_ID_43890 = "43890"
    private const val VALUE_TRACKER_ID_43892 = "43892"
    private const val LABEL_PROGRESSIVE = "progressive"
    private const val LABEL_NON_PROGRESSIVE = "non progressive"
    private const val VALUE_BUSINESS_UNIT = "user platform"
    private const val VALUE_CURRENT_SITE = "tokopediamarketplace"
    private const val AUTO = "auto"
    private const val MANUAL = "manual"
    const val KYC_FLOW_NON_PROGRESSIVE = "non progressive"
    const val PENDING = "pending"
    const val FAILED = "failed"
    const val SUCCESS = "success"

    //A5 , A2i and A3 (A5 wont do)
    fun sendViewKycOnboardingPage(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_ONBOARDING_PAGE)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43324)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A1
    fun sendClickOnButtonBenefit(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_AMBIL_KEUNTUNGAN)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43322)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1a
    fun sendClickOnButtonBackFromOnboardingPage(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43323)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A25
    fun sendClickButtonPakaiKtpIni(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_PAKAI_KTP)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId ")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43623)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1r
    fun sendClickOnButtonTokopediaCareOnboardingPage(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_TOKOPEDIA_CARE)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId ")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43624)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A4
    fun sendClickOnButtonMulaiVerifikasi(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_MULAI_VERIFIKASI)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId ")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43325)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1b
    fun sendClickOnButtonCloseOnboardingBottomSheet(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("${convertGotoKycFlow(kycFlowType)} - $projectId ")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43630)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewOnPendingBottomSheetOnboardingPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_GOPAY_PENDING)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43326)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickCloseOnPendingBottomSheetOnboardingPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_CLOSE_PENDING_PAGE)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43631)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A6i or A6
    fun sendViewStatusPage(kycFlowType: String, status: String, errorReason: String = "", projectId: String) {
        val finalStatus = if (errorReason.isNotEmpty()) {
            "$status - $errorReason"
        } else {
            status
        }
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_STATUS_PAGE)
            .setEventCategory(CATEGORY_STATUS_PAGE)
            .setEventLabel("$kycFlowType - $finalStatus - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43616)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A18 or A15
    fun sendClickOnButtonVerifikasiUlangRejectPage(kycFlowType: String, errorMessage: String, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_VERIFIKASI_ULANG)
            .setEventCategory(CATEGORY_STATUS_PAGE)
            .setEventLabel("$kycFlowType - $FAILED - $errorMessage - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43890)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A26
    fun sendViewDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_DOB_PAGE)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43625)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1s
    fun sendClickOnButtonBackFromDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43892)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A30
    fun sendClickButtonConfirmationDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_CONFIRMATION)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43626)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A31
    fun sendViewDobPageFailed(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_DOB_PAGE_FAILED)
            .setEventCategory(CATEGORY_DOB_PAGE_FAILED)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43627)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendViewOnErrorPageEvent(errorMessage: String, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_ERROR_PAGE)
            .setEventCategory(CATEGORY_ERROR_PAGE)
            .setEventLabel("$errorMessage - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43614)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickOnButtonKirimUlangErrorPageEvent (errorMessage: String, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_KIRIM_ULANG)
            .setEventCategory(CATEGORY_ERROR_PAGE)
            .setEventLabel("$errorMessage - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43615)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B1
    fun sendViewKtpPage(isManual: Boolean, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_KTP_PAGE)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel("${convertCaptureMode(isManual)} - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43327)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendScanKtpImage(statusScan: String, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_SCAN_KTP_IMAGE)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel("$statusScan - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43328)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2a
    fun sendClickOnButtonBackFromKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43632)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B2
    fun sendViewManualFotoKtpQuestion(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_MANUAL_FOTO_QUESTION)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43329)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2g
    fun sendClickButtonFotoManualKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BUTTON_MANUAL)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43330)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2f
    fun sendClickOnButtonFotoOtomatisKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BUTTON_OTOMATIS)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43331)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2e
    fun sendClickOnFotoManualCloseButtonKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_FOTO_MANUAL_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43633)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B3
    fun sendClickOnButtonCaptureKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_CAPTURE)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43333)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2h
    fun sendClickOnButtonBackManualKtp(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK_MANUAL_KTP)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43634)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B4
    fun sendViewGuideSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_GUIDE_SELFIE_PAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43334)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendClickNeedTimeGuideSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_GET_READY_FIRST)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendScanSelfieImage(statusScan: String, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_SCAN_SELFIE_IMAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel("$statusScan - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43338)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2k
    fun sendClickOnStartSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BUTTON_MULAI_SELFIE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43335)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B5
    fun sendViewSelfiePage(isManual: Boolean, projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_SELFIE_PAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel("${convertCaptureMode(isManual)} - $projectId")
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43337)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2l
    fun sendClickOnButtonBackSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BACK_SELFIE_PAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43635)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B6
    fun sendViewManualFotoSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_MANUAL_FOTO_QUESTION)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43339)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2r
    fun sendClickButtonFotoManualSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BUTTON_MANUAL)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43340)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2q
    fun sendClickOnButtonFotoOtomatisSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BUTTON_OTOMATIS)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43341)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2p
    fun sendClickOnFotoManualCloseButtonSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_FOTO_MANUAL_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43815)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B7
    fun sendClickOnButtonCaptureSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_CAPTURE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43343)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2s
    fun sendClickOnButtonBackManualSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK_MANUAL_SELFIE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43816)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B10
    fun sendViewReviewPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_VIEW_REVIEW_PAGE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43602)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2u
    fun sendClickOnButtonBackFromReviewPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43817)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2v
    fun sendClickOnKtpImageReviewPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_KTP_IMAGE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43603)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2x
    fun sendClickButtonRetakeKtp(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_RETAKE_KTP)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43604)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2y
    fun sendClickDeleteKtp(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_DELETE_KTP)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43605)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2z
    fun sendClickSelfieImage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_SELFIE_IMAGE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43606)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ab
    fun sendClickButtonRetakeSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_RETAKE_SELFIE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43607)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ac
    fun sendClickDeleteSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_DELETE_SELFIE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43608)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B11
    fun sendClickButtonKirimDocument(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_ON_BUTTON_KIRIM_DOKUMEN)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43609)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ad
    fun sendClickOnButtonBackFromReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43888)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2af
    fun sendClickOnButtonFotoUlangReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_FOTO_ULANG)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43610)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ag
    fun sendClickOnButtonPakaiFotoIniReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_PAKAI_FOTO_INI)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43611)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ah
    fun sendClickOnButtonBackFromReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43889)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2aj
    fun sendClickOnButtonFotoUlangReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_FOTO_ULANG)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43612)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ak
    fun sendClickOnButtonPakaiFotoIniReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent(EVENT_CLICK_ACCOUNT)
            .setEventAction(ACTION_CLICK_BUTTON_PAKAI_FOTO_INI)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_43613)
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    private fun convertCaptureMode(isManual: Boolean): String {
        return if (isManual) {
            MANUAL
        } else {
            AUTO
        }
    }

    private fun convertGotoKycFlow(kycFlowType: String): String {
        return when (kycFlowType) {
            KYCConstant.GotoKycFlow.PROGRESSIVE -> {
                LABEL_PROGRESSIVE
            }
            KYCConstant.GotoKycFlow.NON_PROGRESSIVE -> {
                LABEL_NON_PROGRESSIVE
            }
            else -> {
                ""
            }
        }
    }

}
