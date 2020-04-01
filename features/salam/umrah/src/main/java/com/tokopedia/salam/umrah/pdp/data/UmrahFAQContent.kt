package com.tokopedia.salam.umrah.pdp.data

import com.google.gson.annotations.SerializedName
/**
 * @author by M on 7/11/19
 */
data class UmrahFAQContent(
        @SerializedName("header")
        val header: String = "",
        @SerializedName("snippet")
        val snippet: String = "",
        @SerializedName("link")
        val link: String = ""
)