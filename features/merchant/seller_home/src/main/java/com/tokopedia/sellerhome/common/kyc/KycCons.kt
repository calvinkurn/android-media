package com.tokopedia.sellerhome.common.kyc

import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform

/**
 * Created by @ilhamsuaib on 4/4/24.
 */

object KycCons {

    object PowerMerchant {
        private const val SOURCE_POWER_MERCHANT = "Power Merchant"
        private const val MERCHANT_KYC_PROJECT_ID = 10
        val KYC_APPLINK = ApplinkConstInternalUserPlatform.getGotoKYCApplink(MERCHANT_KYC_PROJECT_ID.toString(), SOURCE_POWER_MERCHANT)
    }
}
