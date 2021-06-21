package com.tokopedia.applink.powermerchant

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

/**
 * Created By @ilhamsuaib on 11/05/21
 */

object PowerMerchantDeepLinkMapper {

    const val QUERY_PARAM_STATE = "state"
    const val VALUE_STATE_APPROVED = "approved"
    const val VALUE_STATE_STAY = "stay"
    const val VALUE_STATE_AGREED = "agreed"
    const val PM_WEBVIEW_URL = "https://www.tokopedia.com/myshop/power-merchant"

    /**
     * `state` query param possible values : {approved/stay/skip/agreed}
     * approved : Dapatkan keuntungan PM Pro
     * stay : Tetap Jadi Power Merchant
     * agreed : Saya Mengerti
     * */
    fun getInternalAppLinkPmProInterrupt(appLink: String): String {
        val uri = Uri.parse(appLink)
        val state = uri.getQueryParameter(QUERY_PARAM_STATE) ?: ""
        val params = mapOf<String, Any>(QUERY_PARAM_STATE to state)

        val applink = if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME
        } else {
            ApplinkConstInternalSellerapp.SELLER_MENU
        }

        return UriUtil.buildUriAppendParams(applink, params)
    }

    /**
     * if pm pro switch to webview from remote config is true, then redirect to webview
     * else redirect to power merchant page (native)
     */
    fun getPowerMerchantAppLink(context: Context): String {
        return if (isEnablePMSwitchToWebView(context)) {
            PM_WEBVIEW_URL
        } else {
            ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE
        }
    }

    fun isEnablePMSwitchToWebView(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.PM_SWITCH_TO_WEB_VIEW, false)
    }
}