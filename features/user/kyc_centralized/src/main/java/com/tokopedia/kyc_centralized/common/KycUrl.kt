package com.tokopedia.kyc_centralized.common

import com.tokopedia.imageassets.ImageUrl

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import java.util.*

/**
 * @author by nisie on 16/11/18.
 */
object KycUrl {
    private val URL_TERMS_AND_CONDITION = "${TokopediaUrl.getInstance().WEB}help/article/syarat-dan-ketentuan-verifikasi-pengguna"
    const val ICON_NOT_VERIFIED = ImageUrl.ICON_NOT_VERIFIED
    const val ICON_WAITING = ImageUrl.ICON_WAITING
    const val ICON_SUCCESS_VERIFY = ImageUrl.ICON_SUCCESS_VERIFY

    const val ICON_FAIL_VERIFY = ImageUrl.ICON_FAIL_VERIFY
    private const val KYC_BASE_URL = "https://accounts.tokopedia.com/"
    private const val KYC_BASE_URL_STAGING = "https://accounts-staging.tokopedia.com/"

    const val KYC_PARAMS = "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n"
    const val SCAN_FACE_FAIL_NETWORK = ImageUrl.SCAN_FACE_FAIL_NETWORK

    const val SCAN_FACE_FAIL_GENERAL = ImageUrl.SCAN_FACE_FAIL_GENERAL
    const val KYC_BENEFIT_BANNER = ImageUrl.KYC_BENEFIT_BANNER
    const val KYC_BENEFIT_POWER_MERCHANT = ImageUrl.KYC_BENEFIT_POWER_MERCHANT
    const val KYC_BENEFIT_SHIELD = ImageUrl.KYC_BENEFIT_SHIELD
    const val KYC_BENEFIT_CART = ImageUrl.KYC_BENEFIT_CART

    val APPLINK_TERMS_AND_CONDITION = String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, URL_TERMS_AND_CONDITION)

    const val KTP_OK = ImageUrl.KTP_OK
    const val KTP_FAIL = ImageUrl.KTP_FAIL
    const val SELFIE_OK = ImageUrl.SELFIE_OK
    const val SELFIE_FAIL = ImageUrl.SELFIE_FAIL
    const val SCAN_SELFIE = ImageUrl.SCAN_SELFIE
    const val SCAN_KTP = ImageUrl.SCAN_KTP
    const val SCAN_FACE = ImageUrl.SCAN_FACE
    const val KTP_VERIF_OK = ImageUrl.KTP_VERIF_OK
    const val KTP_VERIF_FAIL = ImageUrl.KTP_VERIF_FAIL
    const val FACE_VERIF_OK = ImageUrl.FACE_VERIF_OK
    const val FACE_VERIF_FAIL = ImageUrl.FACE_VERIF_FAIL

    fun getKYCBaseUrl(): String {
        return when (TokopediaUrl.getInstance().TYPE) {
            Env.STAGING -> KYC_BASE_URL_STAGING
            else -> KYC_BASE_URL
        }
    }
}
