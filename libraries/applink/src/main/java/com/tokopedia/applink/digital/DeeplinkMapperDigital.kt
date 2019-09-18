package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.InputStreamReader


object DeeplinkMapperDigital {

    private const val TEMPLATE_VOUCHER = "voucher"
    private val MENU_ID_TELCO = listOf(1, 2)

    /**
     * Cache to variable to speed up performance
     */
    var whiteList: Whitelist? = null

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
        if (!uri.getQueryParameter("template").isNullOrEmpty()) return getDigitalTemplateNavigation(context, deeplink)
        if (!uri.getQueryParameter("menu_id").isNullOrEmpty()) return getDigitalMenuNavigation(context, deeplink)
        return deeplink
    }

    fun getDigitalTemplateNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        var newDeeplink = uri.getQueryParameter("template")?.let {
            when (it) {
                TEMPLATE_VOUCHER -> {
                    // TODO: Enable remote config
//                    if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ENABLE_DIGITAL_VOUCHER_GAME_PDP))
//                            ApplinkConsInternalDigital.VOUCHER_GAME else deeplink
                    ApplinkConsInternalDigital.VOUCHER_GAME
                }
                else -> deeplink
            }
        } ?: deeplink
        if (newDeeplink != deeplink) newDeeplink = "$newDeeplink?${uri.query}"
        return newDeeplink
    }

    fun getDigitalMenuNavigation(context: Context, deeplink: String): String {
        val uri = Uri.parse(deeplink)
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        var newDeeplink = uri.getQueryParameter("menu_id")?.toIntOrNull()?.let {
            when (it) {
                in MENU_ID_TELCO -> {
                    if (remoteConfig.getBoolean(RemoteConfigKey.MAINAPP_ENABLE_DIGITAL_TELCO_PDP))
                        ApplinkConsInternalDigital.TELCO_DIGITAL else deeplink
                }
                else -> deeplink
            }
        } ?: deeplink
        if (newDeeplink != deeplink) newDeeplink = "$newDeeplink?${uri.query}"
        return newDeeplink
    }
}