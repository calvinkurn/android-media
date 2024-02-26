package com.tokopedia.discovery2.data.automatecoupon

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class CtaRedirectionMetadata(
    @SerializedName("url")
    val url: String = "",

    @SerializedName("app_link")
    val appLink: String = "",

    @SerializedName("catalog_id")
    val catalogId: Long = 0
) {

    companion object {

        fun parse(source: String): CtaRedirectionMetadata {
            return try {
                Gson().fromJson(source, CtaRedirectionMetadata::class.java)
            } catch (_: Throwable) {
                CtaRedirectionMetadata()
            }
        }
    }
}
