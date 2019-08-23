package com.tokopedia.applink.digital

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import tokopedia.applink.R
import java.io.BufferedReader
import java.io.InputStreamReader


object DeeplinkMapperDigital {

    private fun readWhitelistFromFile(context: Context): List<WhitelistItem> {
        val inputStream = context.getResources().openRawResource(R.raw.whitelist)
        val reader = BufferedReader(InputStreamReader(inputStream))

        val gson = Gson()
        val whitelist = gson.fromJson(reader, Whitelist::class.java)
        return whitelist.data
    }

    fun getRegisteredNavigationFromHttpDigital(context: Context, deeplink: String): String {
        val path = Uri.parse(deeplink).pathSegments.joinToString("/")
        return readWhitelistFromFile(context).firstOrNull { it.path.equals(path, false) }?.applink ?: ""
    }
}