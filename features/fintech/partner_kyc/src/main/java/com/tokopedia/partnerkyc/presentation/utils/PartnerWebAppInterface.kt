package com.tokopedia.partnerkyc.presentation.utils

import android.webkit.JavascriptInterface
import org.json.JSONObject
import timber.log.Timber

class PartnerWebAppInterface(private val openCamera: (docType: String?) -> Unit) {

    @JavascriptInterface
    fun takePicture(json: String) {
        var finalJson = json
        if (json == UNDEFINED) {
            finalJson = DEFAULT_JSON
        }

        try {
            val jsonObject = JSONObject(finalJson)

            openCamera.invoke(jsonObject.getString(DOCUMENT))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    companion object {
        private const val UNDEFINED = "undefined"
        private const val DEFAULT_JSON = "{\"document\":\"selfie\",\"lang\":\"en\"}"
        private const val DOCUMENT = "document"
    }
}
