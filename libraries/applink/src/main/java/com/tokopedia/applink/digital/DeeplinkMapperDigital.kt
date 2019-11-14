package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.MENU_ID_TELCO
import com.tokopedia.applink.digital.DeeplinkMapperDigitalConst.TEMPLATE_ID_VOUCHER
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.InputStreamReader


object DeeplinkMapperDigital {

    /**
     * Cache to variable to speed up performance
     */
    var whiteList: Whitelist? = null

    const val TEMPLATE_PARAM = "template"
    const val MENU_ID_PARAM = "menu_id"
    const val NFC_CALLING_TYPE = "callingType"

    private fun readWhitelistFromFile(context: Context): List<WhitelistItem> {
        if (whiteList == null) {
            val inputStream = context.getResources().openRawResource(R.raw.whitelist)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val gson = Gson()
            whiteList = gson.fromJson(reader, Whitelist::class.java)
        }
        return whiteList?.data ?: listOf()
    }

    fun getRegisteredNavigationFromHttpDigital(context: Context, deeplink: String): String {
        val path = Uri.parse(deeplink).pathSegments.joinToString("/")
        return readWhitelistFromFile(context).firstOrNull { it.path.equals(path, false) }?.applink ?: ""
    }

    fun getRegisteredNavigationDigital(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (deeplink.startsWith(ApplinkConst.DIGITAL_PRODUCT, true)) {
            if (!uri.getQueryParameter(TEMPLATE_PARAM).isNullOrEmpty()) return getDigitalTemplateNavigation(context, deeplink)
            if (!uri.getQueryParameter(MENU_ID_PARAM).isNullOrEmpty()) return getDigitalMenuNavigation(context, deeplink)
        } else if (deeplink.startsWith(ApplinkConst.DIGITAL_SMARTCARD)){
            return getDigitalSmartcardNavigation(context, deeplink)
        }
        return deeplink
    }

    fun getDigitalTemplateNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        var newDeeplink = uri.getQueryParameter(TEMPLATE_PARAM)?.let {
            when (it) {
                TEMPLATE_ID_VOUCHER -> {
                    // TODO: Enable remote config
                    if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ENABLE_DIGITAL_VOUCHER_GAME_PDP))
                            ApplinkConsInternalDigital.VOUCHER_GAME else deeplink
                    ApplinkConsInternalDigital.VOUCHER_GAME
                }
                else -> deeplink
            }
        } ?: deeplink
        // Append query to new deeplink
        if (newDeeplink != deeplink) newDeeplink = "$newDeeplink?${uri.query}"
        return newDeeplink
    }

    fun getDigitalMenuNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        var newDeeplink = uri.getQueryParameter(MENU_ID_PARAM)?.toIntOrNull()?.let {
            when (it) {
                in MENU_ID_TELCO -> {
                    if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ENABLE_DIGITAL_TELCO_PDP))
                        ApplinkConsInternalDigital.TELCO_DIGITAL else deeplink
                }
                else -> deeplink
            }
        } ?: deeplink
        // Append query to new deeplink
        if (newDeeplink != deeplink) newDeeplink = "$newDeeplink?${uri.query}"
        return newDeeplink
    }

    fun getDigitalSmartcardNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        var paramValue = uri.getQueryParameter(NFC_CALLING_TYPE)


        return if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_SMARTCARD_BRIZZI))
            UriUtil.buildUri(ApplinkConsInternalDigital.SMARTCARD_WITH_BRIZZI, paramValue)
        else
            UriUtil.buildUri(ApplinkConsInternalDigital.SMARTCARD_EMONEY, paramValue)
    }
}