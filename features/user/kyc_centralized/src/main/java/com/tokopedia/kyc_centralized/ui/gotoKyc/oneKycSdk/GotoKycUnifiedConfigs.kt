package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import com.gojek.jago.onekyc.configs.UnifiedKycConfigs
import com.gojek.kyc.sdk.core.utils.KycSdkPartner
import com.tokopedia.applink.ApplinkConst

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
    override fun getPartnerKycFrChallengeQuestionsHelpPageId(kycSdkPartner: KycSdkPartner, onboardingPartner: String): String {
        /*
        * base url (https://www.tokopedia.com/) is set statically for staging and production environments
        * because the url between the both is different and
        * to make it easier to organize logic and content the article, the url set to production only
        * */
        val url = if (kycSdkPartner == KycSdkPartner.FINDAYA_MAB_CICIL)
            getArticleUrl("a-4126")
        else
            getArticleUrl("a-4048")
        return "${ApplinkConst.WEBVIEW}?url=$url"
    }

    // Usage for Toko: Deeplink for Help CTA in Consent Screen
    override fun getPartnerKycConsentHelpPageId(kycSdkPartner: KycSdkPartner): String {
        /*
        * base url (https://www.tokopedia.com/) is set statically for staging and production environments
        * because the url between the both is different and
        * to make it easier to organize logic and content the article, the url set to production only
        * */
        val url = if (kycSdkPartner == KycSdkPartner.FINDAYA_MAB_CICIL)
            getArticleUrl("a-4123")
        else
            getArticleUrl("a-4046")
        return "${ApplinkConst.WEBVIEW}?url=$url"
    }

    // Usage for Toko: Deeplink for Help CTA in Masked User Details Screen
    override fun getPartnerKycUserDetailHelpPageId(kycSdkPartner: KycSdkPartner): String {
        /*
        * base url (https://www.tokopedia.com/) is set statically for staging and production environments
        * because the url between the both is different and
        * to make it easier to organize logic and content the article, the url set to production only
        * */
        val url = if (kycSdkPartner == KycSdkPartner.FINDAYA_MAB_CICIL)
            getArticleUrl("a-4127")
        else
            getArticleUrl("a-4047")
        return "${ApplinkConst.WEBVIEW}?url=$url"
    }

    // Use the given implementation
    override fun isPartnerAuroraEnabled(kycSdkPartner: KycSdkPartner): Boolean {
        return kycSdkPartner != KycSdkPartner.TOKOPEDIA_CORE
    }

    private fun getArticleUrl(endpoint: String): String {
        return "https://www.tokopedia.com/help/article/$endpoint"
    }
}
