package com.tokopedia.applink.shopscore

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 13/04/21
 */

object DeepLinkMapperShopScore {

    const val PARAM_IS_CONSENT = "is_consent"

    fun getShopScoreApplink(deeplink: String): String {
        return if (deeplink.startsWith(ApplinkConst.SHOP_SCORE_DETAIL_ACKNOWLEDGE_INTERRUPT)) {
            getInterruptHandlerPageApplink()
        } else {
            deeplink.replace(ApplinkConst.SHOP_SCORE_DETAIL, ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL)
        }
    }

    private fun getInterruptHandlerPageApplink(): String {
        val param = mapOf<String, Any>(PARAM_IS_CONSENT to true)

        val applink = if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME
        } else {
            ApplinkConstInternalSellerapp.SELLER_MENU
        }

        return UriUtil.buildUriAppendParams(applink, param)
    }
}