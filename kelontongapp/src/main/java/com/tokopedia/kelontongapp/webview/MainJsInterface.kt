package com.tokopedia.kelontongapp.webview

import android.content.Context
import android.webkit.JavascriptInterface
import com.appsflyer.AppsFlyerLib
import org.json.JSONObject
import java.lang.Exception
import java.util.*

const val JS_INTERFACE_NAME = "app"

/**
 * @author okasurya on 10/31/18.
 */
class MainJsInterface(private val context: Context) {

    @JavascriptInterface
    fun trackEvent(name: String, json: String?) {
        var params: Map<String, Any>? = null
        if (json != null) {
            try {
                val jsonObject = JSONObject(json)
                params = HashMap()
                val keys = jsonObject.keys()
                while (keys.hasNext()) {
                    val key: String = keys.next()
                    val value = jsonObject.opt(key)
                    params[key] = value
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        AppsFlyerLib.getInstance().trackEvent(context, name, params)
    }
}