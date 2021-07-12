package com.tokopedia.media.common.common

import com.tokopedia.media.common.data.*

object UrlBuilder {

    fun urlBuilder(
            networkState: String,
            qualitySettings: Int,
            url: String
    ): String {
        val connectionType = when(qualitySettings) {
            LOW_QUALITY_SETTINGS -> LOW_QUALITY // (2g / 3g)
            HIGH_QUALITY_SETTINGS -> HIGH_QUALITY // (4g / wifi)
            else -> networkState
        }

        return if (connectionType == LOW_QUALITY)
            url.addEctParam(connectionType)
        else url
    }

    /**
     * addEctParam()
     * it will add the query parameter of ECT to adopt a adaptive images,
     * if the URL has query parameters, it will append a new string with &ect=connType,
     * but if the URL haven't query parameters yet, it will append with ?ect=connType
     * @param connType (connection type)
     */
    private fun String.addEctParam(connType: String): String {
        return if (hasParam(this)) "$this&$PARAM_ECT=$connType" else "$this?$PARAM_ECT=$connType"
    }

    private fun hasParam(url: String): Boolean {
        return url.contains("?")
    }

}