package com.tokopedia.applink.shopscore

import android.net.Uri
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
    const val COMMUNICATION_PERIOD = "communication_period"
    const val TRANSITION_PERIOD = "transition_period"
    const val PARAM_TYPE = "type"

    fun getShopScoreApplink(deeplink: String): String {
        return if (deeplink.startsWith(ApplinkConst.SHOP_SCORE_DETAIL_ACKNOWLEDGE_INTERRUPT)) {
            getInterruptHandlerPageApplink()
        } else {
            when(Uri.parse(deeplink).getQueryParameter(PARAM_TYPE)) {
                COMMUNICATION_PERIOD -> deeplink.replace(ApplinkConst.SHOP_SCORE_DETAIL, ApplinkConstInternalMarketplace.SHOP_SCORE_DETAIL)
                else -> ApplinkConstInternalMarketplace.SHOP_PERFORMANCE
            }
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