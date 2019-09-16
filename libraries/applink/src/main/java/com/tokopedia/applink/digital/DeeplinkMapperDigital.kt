package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
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
}