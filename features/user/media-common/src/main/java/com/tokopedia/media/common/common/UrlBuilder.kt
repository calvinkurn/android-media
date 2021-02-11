package com.tokopedia.media.common.common

import com.tokopedia.media.common.data.*

object UrlBuilder {

    private val blurHashes = listOf(
            "A4ADcRuO_2y?",
            "A9K{0B#R3WyY",
            "AHHUnD~V^ia~",
            "A2N+X[~qv]IU",
            "ABP?2U~X5J^~"
    )

    fun urlBuilder(
            networkState: String,
            qualitySettings: Int,
            url: String
    ): String {
        var newUrl = url
        val connectionType = when(qualitySettings) {
            LOW_QUALITY_SETTINGS -> LOW_QUALITY // (2g / 3g)
            HIGH_QUALITY_SETTINGS -> HIGH_QUALITY // (4g / wifi)
            else -> networkState
        }

        if (!hasBlurHash(url)) newUrl = newUrl.addBlurHashParam()
        return if (connectionType == LOW_QUALITY) newUrl.addEctParam(connectionType) else newUrl
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

    /**
     * addBlurHashParam()
     * it will add the blur hash on parameter of b=,
     * if the URL has query parameters, it will append a new string with &b=hash,
     * but if the URL haven't query parameters yet, it will append with ?b=hash
     */
    private fun String.addBlurHashParam(): String {
        val hash = blurHashes.random()
        return if (hasParam(this)) "$this&$PARAM_BLURHASH=$hash" else "$this?$PARAM_BLURHASH=$hash"
    }

    private fun hasParam(url: String): Boolean {
        return url.contains("?")
    }

    private fun hasBlurHash(url: String): Boolean {
        return url.contains("$PARAM_BLURHASH=")
    }

}