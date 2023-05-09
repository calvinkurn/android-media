package com.tokopedia.kyc_centralized.ui.gotoKyc.analytics

import com.tokopedia.track.builder.Tracker

object GotoKycAnalytics {

    private const val CATEGORY_ONBOARDING_PAGE= "goto kyc onboarding page"
    private const val CATEGORY_PENDING_PAGE = "goto kyc pending page"
    private const val CATEGORY_SUCCESS_PAGE = "goto kyc success page"
    private const val CATEGORY_REJECT_PAGE = "goto kyc reject page"
    private const val CATEGORY_DOB_PAGE = "goto kyc dob page"
    private const val CATEGORY_DOB_PAGE_FAILED = "goto kyc failed dob page"
    private const val CATEGORY_TIMEOUT_PAGE = "goto kyc timeout page"
    private const val CATEGORY_CONNECTION_ISSUE_PAGE = "goto kyc connection issue page"
    private const val CATEGORY_KTP_PAGE = "goto kyc ktp page"
    private const val CATEGORY_SELFIE_PAGE = "goto kyc selfie page"
    private const val CATEGORY_REVIEW_PAGE = "goto kyc review page"
    private const val CATEGORY_REVIEW_KTP_PAGE = "goto kyc review ktp page"
    private const val CATEGORY_REVIEW_SELFIE_PAGE = "view on kyc review selfie page"
    private const val ACTION_VIEW_KTP_PAGE = "view on kyc ktp page"
    private const val ACTION_VIEW_ONBOARDING_PAGE = "view kyc onboarding page"
    private const val ACTION_CLICK_BUTTON_AMBIL_KEUNTUNGAN = "click on button ambil keuntungannya"
    private const val ACTION_CLICK_BUTTON_BACK = "click on button back"
    private const val ACTION_CLICK_BUTTON_COBA_LAGI = "click on button coba lagi"
    private const val ACTION_CLICK_BUTTON_MULAI_VERIFIKASI = "click on button mulai verifikasi"
    private const val ACTION_CLICK_BUTTON_PAKAI_KTP = "click on button pakai ktp ini"
    private const val ACTION_CLICK_BUTTON_BACK_MANUAL_KTP = "click on button back - manual ktp"
    private const val ACTION_CLICK_BUTTON_BACK_MANUAL_SELFIE = "click on button back - manual selfie"
    private const val ACTION_CLICK_BUTTON_PAKAI_FOTO_INI = "click on button pakai foto ini"
    private const val ACTION_CLICK_CLOSE_BUTTON = "click on close button"
    private const val ACTION_CLICK_BALIK_HALAMAN_UTAMA = "click on button balik halaman utama"
    private const val ACTION_CLICK_VERIFIKASI_ULANG = "click on button verifikasi ulang"
    private const val ACTION_CLICK_TOKOPEDIA_CARE = "click on button hubungi tokopedia care"
    private const val ACTION_CLICK_CLOSE_CHOOSE_BIRTHDATE = "click on close pilih tanggal lahir"
    private const val ACTION_CLICK_CHOOSE_BIRTHDATE = "click on pilih tanggal lahir"
    private const val ACTION_CLICK_BUTTON_CONFIRMATION = "click on button konfirmasi"
    private const val ACTION_CLICK_BUTTON_SETTING = "click on button ke pengaturan"
    private const val ACTION_CLICK_BUTTON_CAPTURE = "click on button capture"
    private const val ACTION_CLICK_BUTTON_LIHAT_TIPS = "click on button lihat tips"
    private const val ACTION_CLICK_BUTTON_FOTO_ULANG = "click on button foto ulang"
    private const val ACTION_CLICK_ON_TIPS_CLOSE_BUTTON = "click on tips - close button"
    private const val ACTION_CLICK_ON_FOTO_MANUAL_CLOSE_BUTTON = "click on foto manual - close button"
    private const val ACTION_CLICK_ON_BUTTON_MANUAL = "click on button foto manual"
    private const val ACTION_CLICK_ON_BUTTON_OTOMATIS = "click on button foto otomatis"
    private const val ACTION_CLICK_ON_BUTTON_MULAI_SELFIE = "click on button mulai selfie"
    private const val ACTION_CLICK_ON_BUTTON_KIRIM_DOKUMEN = "click on button kirim dokumen"
    private const val ACTION_CLICK_ON_BACK_SELFIE_PAGE = "click on button back - selfie page"
    private const val ACTION_CLICK_BUTTON_LIHAT_TIPS_SELFIE = "click on button lihat tips selfie"
    private const val ACTION_CLICK_BUTTON_LIHAT_TIPS_KTP = "click on button lihat tips selfie"
    private const val ACTION_CLICK_RETAKE_KTP = "click on button retake ktp"
    private const val ACTION_CLICK_DELETE_KTP = "click on button delete ktp"
    private const val ACTION_CLICK_RETAKE_SELFIE = "click on button retake selfie"
    private const val ACTION_CLICK_DELETE_SELFIE = "click on button delete selfie"
    private const val ACTION_CLICK_KTP_IMAGE = "click on ktp image"
    private const val ACTION_CLICK_SELFIE_IMAGE = "click on selfie image"
    private const val ACTION_VIEW_MANUAL_FOTO_QUESTION = "view on manual foto question"
    private const val ACTION_VIEW_PENDING_PAGE = "view on kyc pending page"
    private const val ACTION_VIEW_SUCCESS_PAGE = "view on kyc success page"
    private const val ACTION_VIEW_REJECT_PAGE = "view on kyc reject page"
    private const val ACTION_VIEW_DOB_PAGE = "view on kyc dob page"
    private const val ACTION_VIEW_DOB_PAGE_FAILED = "view on kyc failed dob page"
    private const val ACTION_VIEW_TIMEOUT_PAGE = "view on kyc timeout page"
    private const val ACTION_VIEW_CONNECTION_ISSUE_PAGE = "view on kyc connection issue page"
    private const val ACTION_VIEW_GUIDE_SELFIE_PAGE = "view on persiapan selfie page"
    private const val ACTION_VIEW_SELFIE_PAGE = "view on selfie page"
    private const val ACTION_VIEW_REVIEW_PAGE = "view on kyc review page"
    private const val ACTION_VIEW_REVIEW_KTP_PAGE = "view on kyc review ktp page"
    private const val ACTION_VIEW_REVIEW_SELFIE_PAGE = "view on kyc review selfie page"
    private const val ACTION_SCAN_KTP_IMAGE = "scan ktp image"
    private const val LABEL_PROGRESSIVE = "progressive"
    private const val LABEL_NON_PROGRESSIVE = "non progressive"
    private const val KEY_PROJECT_ID = "trackerId"
    private const val VALUE_BUSINESS_UNIT = "user platform"
    private const val VALUE_CURRENT_SITE = "tokopediamarketplace"

    //A5
    fun sendViewKycOnboardingPage(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_ONBOARDING_PAGE)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A1
    fun sendClickOnButtonAmbilKeuntungan(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_AMBIL_KEUNTUNGAN)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1a
    fun sendClickOnButtonBackFromOnboardingPage(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A25
    fun sendClickButtonPakaiKtpIni(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_PAKAI_KTP)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1r
    fun sendClickOnButtonTokopediaCareOnboardingPage(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_TOKOPEDIA_CARE)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A4
    fun sendClickOnButtonMulaiVerifikasi(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_MULAI_VERIFIKASI)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1b
    fun sendClickOnButtonClose(projectId: String, kycFlowType: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_ONBOARDING_PAGE)
            .setEventLabel("$projectId - $kycFlowType")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A6i or A6
    fun sendViewPendingPage(processingTime: String, projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_PENDING_PAGE)
            .setEventCategory(CATEGORY_PENDING_PAGE)
            .setEventLabel("$processingTime - $projectId")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1g or 1e
    fun sendClickOnButtonBackFromPendingPage(processingTime: String, projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_PENDING_PAGE)
            .setEventLabel("$processingTime - $projectId")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A8
    fun sendClickOnButtonBalikPendingPage(processingTime: String, projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BALIK_HALAMAN_UTAMA)
            .setEventCategory(CATEGORY_PENDING_PAGE)
            .setEventLabel("$processingTime - $projectId")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A11 or A13
    fun sendViewSuccessPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_SUCCESS_PAGE)
            .setEventCategory(CATEGORY_SUCCESS_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1i or 1j
    fun sendClickOnButtonBackFromSuccessPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_SUCCESS_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }


    //A12
    fun sendClickOnButtonBalikSuccessPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BALIK_HALAMAN_UTAMA)
            .setEventCategory(CATEGORY_SUCCESS_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A17 or A14
    fun sendViewRejectPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_REJECT_PAGE)
            .setEventCategory(CATEGORY_REJECT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1L or 1k
    fun sendClickOnButtonBackFromRejectPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REJECT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A18 or A15
    fun sendClickOnButtonVerifikasiUlangRejectPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_VERIFIKASI_ULANG)
            .setEventCategory(CATEGORY_REJECT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A19 or A16
    fun sendClickOnButtonTokopediaCareRejectPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_TOKOPEDIA_CARE)
            .setEventCategory(CATEGORY_REJECT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A26
    fun sendViewDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_DOB_PAGE)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1s
    fun sendClickOnButtonBackFromDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1t
    fun sendClickOnCloseChooseBirthDate(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_CLOSE_CHOOSE_BIRTHDATE)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A29
    fun sendClickOnChooseBirthDate(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_CHOOSE_BIRTHDATE)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A30
    fun sendClickButtonConfirmationDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_CONFIRMATION)
            .setEventCategory(CATEGORY_DOB_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A31
    fun sendViewDobPageFailed(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_DOB_PAGE_FAILED)
            .setEventCategory(CATEGORY_DOB_PAGE_FAILED)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A32
    fun sendClickOnButtonBalikFailedDobPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BALIK_HALAMAN_UTAMA)
            .setEventCategory(CATEGORY_DOB_PAGE_FAILED)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A33
    fun sendViewTimeoutPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_TIMEOUT_PAGE)
            .setEventCategory(CATEGORY_TIMEOUT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1u
    fun sendClickOnButtonBackFromTimeoutPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_TIMEOUT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }


    //1v
    fun sendClickOnCobaLagiTimeoutPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_COBA_LAGI)
            .setEventCategory(CATEGORY_TIMEOUT_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //A34
    fun sendViewConnectionIssuePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_CONNECTION_ISSUE_PAGE)
            .setEventCategory(CATEGORY_CONNECTION_ISSUE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1w
    fun sendClickOnButtonBackFromConnectionIssuePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_CONNECTION_ISSUE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1x
    fun sendClickOnCobaLagiConnectionIssuePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_COBA_LAGI)
            .setEventCategory(CATEGORY_CONNECTION_ISSUE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //1y
    fun sendClickOnButtonSettingConnectionIssuePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_SETTING)
            .setEventCategory(CATEGORY_CONNECTION_ISSUE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B1
    fun sendViewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_KTP_PAGE)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    fun sendScanKtpImage(statusScan: String, projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_SCAN_KTP_IMAGE)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel("$statusScan - $projectId")
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2a
    fun sendClickOnButtonBackFromKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2b
    fun sendClickOnButtonLihatTipsKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_LIHAT_TIPS)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2c
    fun sendClickOnTipsCloseButtonKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_TIPS_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B2
    fun sendViewManualFotoKtpQuestion(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_MANUAL_FOTO_QUESTION)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2g
    fun sendClickButtonFotoManualKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BUTTON_MANUAL)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2f
    fun sendClickOnButtonFotoOtomatisKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BUTTON_OTOMATIS)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2e
    fun sendClickOnFotoManualCloseButtonKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_FOTO_MANUAL_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B3
    fun sendClickOnButtonCaptureKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_CAPTURE)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2h
    fun sendClickOnButtonBackManualKtp(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK_MANUAL_KTP)
            .setEventCategory(CATEGORY_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B4
    fun sendViewGuideSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_GUIDE_SELFIE_PAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2k
    fun sendClickOnStartSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BUTTON_MULAI_SELFIE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B5
    fun sendViewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_SELFIE_PAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2l
    fun sendClickOnButtonBackSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BACK_SELFIE_PAGE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2m
    fun sendClickOnButtonLihatTipsSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_LIHAT_TIPS)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2c
    fun sendClickOnTipsCloseButtonSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_TIPS_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B6
    fun sendViewManualFotoSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_MANUAL_FOTO_QUESTION)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2r
    fun sendClickButtonFotoManualSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BUTTON_MANUAL)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2q
    fun sendClickOnButtonFotoOtomatisSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BUTTON_OTOMATIS)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2p
    fun sendClickOnFotoManualCloseButtonSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_FOTO_MANUAL_CLOSE_BUTTON)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B7
    fun sendClickOnButtonCaptureSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_CAPTURE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2s
    fun sendClickOnButtonBackManualSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK_MANUAL_SELFIE)
            .setEventCategory(CATEGORY_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B10
    fun sendViewReviewPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_REVIEW_PAGE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2u
    fun sendClickOnButtonBackFromReviewPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2v
    fun sendClickOnKtpImageReviewPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_KTP_IMAGE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2w
    fun sendClickButtonLihatTipsKtp(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_LIHAT_TIPS_KTP)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2x
    fun sendClickButtonRetakeKtp(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_RETAKE_KTP)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2y
    fun sendClickDeleteKtp(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_DELETE_KTP)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2z
    fun sendClickSelfieImage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_SELFIE_IMAGE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2aa
    fun sendClickButtonLihatTipsSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_LIHAT_TIPS_SELFIE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ab
    fun sendClickButtonRetakeSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_RETAKE_SELFIE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ac
    fun sendClickDeleteSelfie(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_DELETE_SELFIE)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B11
    fun sendClickButtonKirimDocument(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_ON_BUTTON_KIRIM_DOKUMEN)
            .setEventCategory(CATEGORY_REVIEW_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B12
    fun sendViewReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_REVIEW_KTP_PAGE)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ad
    fun sendClickOnButtonBackFromReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ae
    fun sendClickOnButtonLihatTipsReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_LIHAT_TIPS)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2af
    fun sendClickOnButtonFotoUlangReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_FOTO_ULANG)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ag
    fun sendClickOnButtonPakaiFotoIniReviewKtpPage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_PAKAI_FOTO_INI)
            .setEventCategory(CATEGORY_REVIEW_KTP_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //B13
    fun sendViewReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_VIEW_REVIEW_SELFIE_PAGE)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ah
    fun sendClickOnButtonBackFromReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_BACK)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ai
    fun sendClickOnButtonLihatTipsReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_LIHAT_TIPS)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2aj
    fun sendClickOnButtonFotoUlangReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_FOTO_ULANG)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

    //2ak
    fun sendClickOnButtonPakaiFotoIniReviewSelfiePage(projectId: String) {
        Tracker.Builder()
            .setEvent("")
            .setEventAction(ACTION_CLICK_BUTTON_PAKAI_FOTO_INI)
            .setEventCategory(CATEGORY_REVIEW_SELFIE_PAGE)
            .setEventLabel(projectId)
            .setCustomProperty(KEY_PROJECT_ID, "")
            .setBusinessUnit(VALUE_BUSINESS_UNIT)
            .setCurrentSite(VALUE_CURRENT_SITE)
            .build()
            .send()
    }

}
