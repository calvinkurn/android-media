package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

/**
 * Function to map the deeplink to applink (registered in manifest)
 *
 * Example when there are 2 deeplink that has the same pattern:
 * tokopedia://product/add and tokopedia://product/{id}
 * tokopedia://product/add will be mapped to tokopedia-android-internal:// to prevent conflict.
 */
object DeeplinkMapper {

    /**
     * Get registered deeplink navigation in manifest
     * In conventional term, convert deeplink (http or tokopedia) to applink (tokopedia:// or tokopedia-android-internal://)
     */
    @JvmStatic
    fun getRegisteredNavigation(context: Context, deeplink: String): String {
        if (deeplink.startsWith(DeeplinkConstant.SCHEME_HTTP, true)) {
            return getRegisteredNavigationFromHttp(context, deeplink)
        } else if (deeplink.startsWith(DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH, true)) {
            return getRegisteredNavigationFromTokopedia(deeplink)
        } else if (deeplink.startsWith(DeeplinkConstant.SCHEME_SELLERAPP, true)) {
            return getRegisteredNavigationFromSellerapp(deeplink)
        } else {
            return deeplink
        }
    }

    /**
     * Mapping http link to registered deplink in manifest (to deeplink tokopedia or tokopedia-android-internal)
     * Due to no differentiation structure link for shop, product with other link (www.tokopedia.com/{shop_domain}/{product_name})
     * the app need translate
     * This function should be called after checking domain shop from server side
     * eg: https://www.tokopedia.com/pulsa/ to tokopedia://pulsa
     */
    private fun getRegisteredNavigationFromHttp(context: Context, deeplink: String): String {
        val applinkDigital = DeeplinkMapperDigital.getRegisteredNavigationFromHttpDigital(context, deeplink)
        if (applinkDigital.isNotEmpty()) {
            return applinkDigital
        }
        return ""
    }

    /**
     * Mapping tokopedia link to registered deplink in manifest if necessary
     * eg: tokopedia://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     *
     * Use this only of the tokopedia deeplink is conflicted with the other tokopedia deeplink
     * for example: tokopedia://product/{id} conflicts with tokopedia://product/add
     */
    private fun getRegisteredNavigationFromTokopedia(deeplink: String): String {
         when (deeplink) {
            ApplinkConst.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
            ApplinkConst.SETTING_PROFILE -> return ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.DISCOVERY_SEARCH -> return ApplinkConstInternalDiscovery.SEARCH_RESULT
            ApplinkConst.SETTING_NOTIFICATION -> return ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            ApplinkConst.GROUPCHAT_LIST -> return ApplinkConstInternalMarketplace.GROUPCHAT_LIST
            ApplinkConst.GROUPCHAT_DETAIL -> return ApplinkConstInternalMarketplace.GROUPCHAT_DETAIL
            ApplinkConst.KYC -> return ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO
            ApplinkConst.SETTING_BANK -> return ApplinkConstInternalGlobal.SETTING_BANK
        }
        when {
            specialNavigationMapper(deeplink, ApplinkConst.Play.HOST) -> {
                return UriUtil.buildUri(ApplinkConstInternalMarketplace.GROUPCHAT_DETAIL, getSegments(deeplink).first())
            }
        }
        return ""
    }

    private fun getSegments(deeplink: String): List<String> {
        return Uri.parse(deeplink).pathSegments
    }

    private fun specialNavigationMapper(deeplink: String, host: String): Boolean {
        val uri = Uri.parse(deeplink)
        return uri.scheme == ApplinkConst.APPLINK_CUSTOMER_SCHEME
                && uri.host == host
                && uri.pathSegments.size > 0
    }

    /**
     * Mapping sellerapp link to registered deplink in manifest if necessary
     * eg: sellerapp://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
     *
     * for example: sellerapp://product/{id} conflicts with sellerapp://product/add
     */
    private fun getRegisteredNavigationFromSellerapp(deeplink: String): String {
        return when (deeplink) {
            ApplinkConst.SellerApp.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
            ApplinkConst.SETTING_PROFILE -> return ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.SETTING_BANK -> return ApplinkConstInternalGlobal.SETTING_BANK
            else -> ""
        }
    }
}