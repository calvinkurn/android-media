package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import com.gojek.jago.onekyc.configs.UnifiedKycConfigs
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.url.TokopediaUrl

class GotoKycUnifiedConfigs : UnifiedKycConfigs {
    // Use the given implementation
    override fun isJagoUnifiedKycEnabled(): Boolean {
        return true
    }

    // Not used
    override fun getGoPayKycStatusPendingHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Not used
    override fun getPartnerKycStatusPendingHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Not used
    override fun getPartnerKycStatusRejectedHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Not used
    override fun getPartnerKycIdentityShareRejectedHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Not used
    override fun getGopayKycStatusRejectedHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Not used
    override fun getGopayKycStatusPollingInterval(): Double {
        return 0.0
    }

    // Not used
    override fun getPartnerAccountBlockedHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Usage for Toko: Deeplink for Help CTA in Challenge Screen(Dob/NIK)
    // toko not use this
    override fun getPartnerKycChallengeQuestionsHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return ""
    }

    // Usage for Toko: Deeplink for Help CTA in Challenge Screen(FR Challenge)
    override fun getPartnerKycFrChallengeQuestionsHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return "${ApplinkConst.WEBVIEW}?url=${TokopediaUrl.getInstance().WEB}help/article/a-4048"
    }

    // Usage for Toko: Deeplink for Help CTA in Consent Screen
    override fun getPartnerKycConsentHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return "${ApplinkConst.WEBVIEW}?url=${TokopediaUrl.getInstance().WEB}help/article/a-4046"
    }

    // Usage for Toko: Deeplink for Help CTA in Masked User Details Screen
    override fun getPartnerKycUserDetailHelpPageId(kycSdkPartner: KycSdkPartner): String {
        return "${ApplinkConst.WEBVIEW}?url=${TokopediaUrl.getInstance().WEB}help/article/a-4047"
    }

    // Use the given implementation
    override fun isPartnerAuroraEnabled(kycSdkPartner: KycSdkPartner): Boolean {
        return kycSdkPartner != KycSdkPartner.TOKOPEDIA_CORE
    }
}
