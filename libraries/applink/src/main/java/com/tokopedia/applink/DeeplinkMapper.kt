package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.Digital_Deals.DeeplinkMapperDeals.getRegisteredNavigationDeals
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.digital.DeeplinkMapperDigital
import com.tokopedia.applink.digital.DeeplinkMapperDigital.getRegisteredNavigationDigital
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalOperational
import com.tokopedia.applink.marketplace.DeeplinkMapperMarketplace.getRegisteredNavigationMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.promo.getRegisteredNavigationTokopoints
import com.tokopedia.applink.search.DeeplinkMapperSearch.getRegisteredNavigationSearch
import com.tokopedia.config.GlobalConfig
import com.tokopedia.applink.internal.ApplinkConstInternalTravel

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
        /**
            If deeplink have query parameters then we need to keep the query and map the url without query
        */
        val mappedDeepLink: String = when {
            deeplink.startsWith(DeeplinkConstant.SCHEME_HTTP, true) -> getRegisteredNavigationFromHttp(context, deeplink)
            deeplink.startsWith(DeeplinkConstant.SCHEME_TOKOPEDIA_SLASH, true) -> {
                when {
                    deeplink.startsWith(ApplinkConst.DIGITAL_PRODUCT, true) ->
                        getRegisteredNavigationDigital(context, deeplink)
                    deeplink.startsWith(ApplinkConst.DISCOVERY_SEARCH, true) ->
                        getRegisteredNavigationSearch(deeplink)
                    deeplink.startsWith(ApplinkConst.CART) || deeplink.startsWith(ApplinkConst.CHECKOUT) ->
                        getRegisteredNavigationMarketplace(deeplink)
                    deeplink.startsWithPattern(ApplinkConst.DEALS_HOME) ->
                        getRegisteredNavigationDeals(deeplink)
                    GlobalConfig.isSellerApp() && deeplink.startsWith(ApplinkConst.HOME) ->
                        ApplinkConst.SellerApp.SELLER_APP_HOME
                    deeplink.startsWith(ApplinkConst.TOKOPOINTS) -> getRegisteredNavigationTokopoints(context, deeplink)

                    else -> {
                        val query = Uri.parse(deeplink).query
                        if(query?.isNotEmpty() == true){
                            val tempDL = deeplink.substring(0, deeplink.indexOf('?'))
                            var navFromTokopedia = getRegisteredNavigationFromTokopedia(tempDL)
                            if(navFromTokopedia.isNotEmpty()) {
                                navFromTokopedia = navFromTokopedia.substring(0, navFromTokopedia.indexOf('?'))
                                navFromTokopedia += "?$query"
                            }
                            navFromTokopedia
                        } else getRegisteredNavigationFromTokopedia(deeplink)
                    }
                }
            }
            deeplink.startsWith(DeeplinkConstant.SCHEME_SELLERAPP, true) -> getRegisteredNavigationFromSellerapp(deeplink)
            else -> deeplink
        }
        return mappedDeepLink
    }

    /**
     * Mapping http link to registered deplink in manifest (to deeplink tokopedia or tokopedia-android-internal)
     * Due to no differentiation structure link for shop, product with other link (www.tokopedia.com/{shop_domain}/{product_name})
     * the app need translate
     * This function should be called after checking domain shop from server side
     * eg: https://www.tokopedia.com/pulsa/ to tokopedia://pulsa
     */
    fun getRegisteredNavigationFromHttp(context: Context, deeplink: String): String {
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
     */
    private fun getRegisteredNavigationFromTokopedia(deeplink: String): String {
        return when (deeplink) {
            ApplinkConst.PRODUCT_ADD -> return ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM
            ApplinkConst.SETTING_PROFILE -> return ApplinkConstInternalGlobal.SETTING_PROFILE
            ApplinkConst.SETTING_NOTIFICATION -> return ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING
            ApplinkConst.KYC_NO_PARAM -> return ApplinkConstInternalGlobal.USER_IDENTIFICATION_INFO
            ApplinkConst.KYC_FORM_NO_PARAM -> return ApplinkConstInternalGlobal.USER_IDENTIFICATION_FORM
            ApplinkConst.SETTING_BANK -> return ApplinkConstInternalGlobal.SETTING_BANK
            ApplinkConst.FLIGHT -> return ApplinkConstInternalTravel.DASHBOARD_FLIGHT
            ApplinkConst.SALDO -> return ApplinkConstInternalGlobal.SALDO_DEPOSIT
            ApplinkConst.SALDO_INTRO -> return ApplinkConstInternalGlobal.SALDO_INTRO
            ApplinkConst.INBOX_TICKET -> return ApplinkConstInternalOperational.INTERNAL_INBOX_LIST
            ApplinkConst.INSTANT_LOAN -> return ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN
            ApplinkConst.INSTANT_LOAN_TAB -> ApplinkConstInternalGlobal.GLOBAL_INTERNAL_INSTANT_LOAN_TAB
            ApplinkConst.PINJAMAN_ONLINE_TAB -> ApplinkConstInternalGlobal.GLOBAL_INTERNAL_PINJAMAN_ONLINE_TAB
            ApplinkConst.PRODUCT_MANAGE -> return ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
            else -> ""
        }
    }

    /**
     * Mapping sellerapp link to registered deplink in manifest if necessary
     * eg: sellerapp://product/add to tokopedia-android-internal://marketplace/product-add-item
     * If not found, return current deeplink, means it registered
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