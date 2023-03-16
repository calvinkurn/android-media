package com.tokopedia.kyc_centralized.common

import com.tokopedia.imageassets.TokopediaImageUrl

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import java.util.*

/**
 * @author by nisie on 16/11/18.
 */
object KycUrl {
    private val URL_TERMS_AND_CONDITION = "${TokopediaUrl.getInstance().WEB}help/article/syarat-dan-ketentuan-verifikasi-pengguna"
    const val ICON_NOT_VERIFIED = TokopediaImageUrl.ICON_NOT_VERIFIED
    const val ICON_WAITING = TokopediaImageUrl.ICON_WAITING
    const val ICON_SUCCESS_VERIFY = TokopediaImageUrl.ICON_SUCCESS_VERIFY

    const val ICON_FAIL_VERIFY = TokopediaImageUrl.ICON_FAIL_VERIFY
    private const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    private const val KYC_BASE_URL_STAGING = "https://accounts-staging.tokopedia.com/"

    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"
    const val SCAN_FACE_FAIL_NETWORK = TokopediaImageUrl.SCAN_FACE_FAIL_NETWORK

    const val SCAN_FACE_FAIL_GENERAL = TokopediaImageUrl.SCAN_FACE_FAIL_GENERAL
    const val KYC_BENEFIT_BANNER = TokopediaImageUrl.KYC_BENEFIT_BANNER
    const val KYC_BENEFIT_POWER_MERCHANT = TokopediaImageUrl.KYC_BENEFIT_POWER_MERCHANT
    const val KYC_BENEFIT_SHIELD = TokopediaImageUrl.KYC_BENEFIT_SHIELD
    const val KYC_BENEFIT_CART = TokopediaImageUrl.KYC_BENEFIT_CART

    val APPLINK_TERMS_AND_CONDITION = String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION)

    const val KTP_OK = TokopediaImageUrl.KTP_OK
    const val KTP_FAIL = TokopediaImageUrl.KTP_FAIL
    const val SELFIE_OK = TokopediaImageUrl.SELFIE_OK
    const val SELFIE_FAIL = TokopediaImageUrl.SELFIE_FAIL
    const val SCAN_SELFIE = TokopediaImageUrl.SCAN_SELFIE
    const val SCAN_KTP = TokopediaImageUrl.SCAN_KTP
    const val SCAN_FACE = TokopediaImageUrl.SCAN_FACE
    const val KTP_VERIF_OK = TokopediaImageUrl.KTP_VERIF_OK
    const val KTP_VERIF_FAIL = TokopediaImageUrl.KTP_VERIF_FAIL
    const val FACE_VERIF_OK = TokopediaImageUrl.FACE_VERIF_OK
    const val FACE_VERIF_FAIL = TokopediaImageUrl.FACE_VERIF_FAIL

    fun getKYCBaseUrl(): String {
        return when (TokopediaUrl.getInstance().TYPE) {
            Env.STAGING -> KYC_BASE_URL_STAGING
            else -> KYC_BASE_URL
        }
    }
}
