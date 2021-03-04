package com.tokopedia.kyc_centralized

/**
 * @author by nisie on 16/11/18.
 */
object KycUrl {
    const val ICON_NOT_VERIFIED = "https://ecs7.tokopedia.net/android/others/account_verification_landing_page.png"
    const val ICON_WAITING = "https://ecs7.tokopedia.net/android/others/account_verification_processing_document.png"
    const val ICON_SUCCESS_VERIFY = "https://ecs7.tokopedia.net/img/android/others/account_verification_verified.png"
    const val ICON_FAIL_VERIFY = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_page.png"

    const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"

    const val SCAN_FACE_FAIL_NETWORK = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_badnetwork.png"
    const val SCAN_FACE_FAIL_GENERAL = "https://ecs7.tokopedia.net/img/android/others/account_verification_failed_general.png"
}