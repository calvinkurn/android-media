package com.tokopedia.kyc_centralized.common

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import java.util.*

/**
 * @author by nisie on 16/11/18.
 */
object KycUrl {
    private val URL_TERMS_AND_CONDITION = "${TokopediaUrl.getInstance().WEB}help/article/syarat-dan-ketentuan-verifikasi-pengguna"
    const val ICON_NOT_VERIFIED = "https://images.tokopedia.net/android/others/account_verification_landing_page.png"
    const val ICON_WAITING = "https://images.tokopedia.net/android/others/account_verification_processing_document.png"
    const val ICON_SUCCESS_VERIFY = "https://images.tokopedia.net/img/android/others/account_verification_verified.png"

    const val ICON_FAIL_VERIFY = "https://images.tokopedia.net/img/android/user/kyc/kyc_rejected.png"
    private const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    private const val KYC_BASE_URL_STAGING = "https://accounts-staging.tokopedia.com/"

    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"
    const val SCAN_FACE_FAIL_NETWORK = "https://images.tokopedia.net/img/android/others/account_verification_failed_badnetwork.png"

    const val SCAN_FACE_FAIL_GENERAL = "https://images.tokopedia.net/img/android/others/account_verification_failed_general.png"
    const val KYC_BENEFIT_BANNER = "https://images.tokopedia.net/img/android/user/kyc_benefit_banner.png"
    const val KYC_BENEFIT_POWER_MERCHANT = "https://images.tokopedia.net/img/android/user/kyc_benefit_power_merchant.png"
    const val KYC_BENEFIT_SHIELD = "https://images.tokopedia.net/img/android/user/kyc_benefit_shield_star.png"
    const val KYC_BENEFIT_CART = "https://images.tokopedia.net/img/android/user/kyc_centralized/kyc_benefit_cart.png"

    val APPLINK_TERMS_AND_CONDITION = String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION)

    const val KTP_OK = "https://images.tokopedia.net/img/android/others/ktp_ok.png"
    const val KTP_FAIL = "https://images.tokopedia.net/img/android/others/ktp_fail.png"
    const val SELFIE_OK = "https://images.tokopedia.net/img/android/others/selfie_ok.png"
    const val SELFIE_FAIL = "https://images.tokopedia.net/img/android/others/selfie_fail.png"
    const val SCAN_SELFIE = "https://images.tokopedia.net/img/android/user/kyc_centralized/img_selfie.png"
    const val SCAN_KTP = "https://images.tokopedia.net/android/others/account_verification_ktp_onboarding.png"
    const val SCAN_FACE = "https://images.tokopedia.net/lottie/scanning_the_face.json"
    const val KTP_VERIF_OK = "https://images.tokopedia.net/img/android/others/account_verification_ktp_success_small.png"
    const val KTP_VERIF_FAIL = "https://images.tokopedia.net/img/android/others/account_verification_ktp_failed_small.png"
    const val FACE_VERIF_OK = "https://images.tokopedia.net/img/android/others/account_verification_liveness_success_small.png"
    const val FACE_VERIF_FAIL = "https://images.tokopedia.net/img/android/others/scan_verification_liveness_failed_small.png"

    fun getKYCBaseUrl(): String {
        return when (TokopediaUrl.getInstance().TYPE) {
            Env.STAGING -> KYC_BASE_URL_STAGING
            else -> KYC_BASE_URL
        }
    }
}
