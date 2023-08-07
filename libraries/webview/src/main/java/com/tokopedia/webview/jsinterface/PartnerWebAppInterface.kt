package com.tokopedia.webview.jsinterface

import android.webkit.JavascriptInterface
import org.json.JSONObject
import timber.log.Timber

class PartnerWebAppInterface(val listener: Listener) {

    interface Listener {
        fun takePictureFromCamera(docType: String, lang: String)
    }

    @JavascriptInterface
    fun takePicture(json: String) {
        var finalJson = json
        if (json == UNDEFINED) {
            finalJson = DEFAULT_JSON
        }

        try {
            val jsonObject = JSONObject(finalJson)
            val param = jsonObject.getString(DOCUMENT)
            listener.takePictureFromCamera(param, "")
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        private const val UNDEFINED = "undefined"
        private const val DEFAULT_JSON = "{\"document\":\"selfie\",\"lang\":\"en\"}"
        private const val DOCUMENT = "document"
        const val CAMERA_PICKER = "CameraPicker"
    }
}
