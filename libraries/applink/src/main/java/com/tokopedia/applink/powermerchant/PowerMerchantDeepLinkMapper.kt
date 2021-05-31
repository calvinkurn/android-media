package com.tokopedia.applink.powermerchant

import android.net.Uri
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 11/05/21
 */

object PowerMerchantDeepLinkMapper {

    const val QUERY_PARAM_STATE = "state"
    const val VALUE_STATE_APPROVED = "approved"
    const val VALUE_STATE_STAY = "stay"
    const val VALUE_STATE_AGREED = "agreed"

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
}