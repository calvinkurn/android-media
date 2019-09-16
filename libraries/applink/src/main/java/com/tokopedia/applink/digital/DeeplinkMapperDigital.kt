package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.applink.internal.ApplinkConsInternalDigital
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.InputStreamReader


object DeeplinkMapperDigital {

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

    fun getRegisteredNavigationDigital(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        if (!uri.getQueryParameter("template").isNullOrEmpty()) return getDigitalTemplateNavigation(deeplink)
        if (!uri.getQueryParameter("menu_id").isNullOrEmpty()) return getDigitalMenuNavigation(deeplink)
        return deeplink
    }

    fun getDigitalTemplateNavigation(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        var newDeeplink = uri.getQueryParameter("template")?.let {
            when (it) {
                "voucher" -> ApplinkConsInternalDigital.VOUCHER_GAME
                else -> deeplink
            }
        } ?: deeplink
        if (newDeeplink != deeplink) newDeeplink = "$newDeeplink?${uri.query}"
        return newDeeplink
    }

    fun getDigitalMenuNavigation(deeplink: String): String {
        val uri = Uri.parse(deeplink)
        var newDeeplink = uri.getQueryParameter("menu_id")?.toIntOrNull()?.let {
            when (it) {
                in 1..2 -> ApplinkConsInternalDigital.TELCO_DIGITAL
                else -> deeplink
            }
        } ?: deeplink
        if (newDeeplink != deeplink) newDeeplink = "$newDeeplink?${uri.query}"
        return newDeeplink
    }
}