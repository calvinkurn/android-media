package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class PageInfo(

        @SerializedName("Path")
        val path: String? = "",

        @SerializedName("meta_title")
        val metaTitle: String? = "",

        @SerializedName("TTL")
        val tTL: Int = 0,

        @SerializedName("Name")
        val name: String? = "",

        @SerializedName("og_image")
        val ogImage: String? = "",

        @SerializedName("robot")
        val robot: String? = "",

        @SerializedName("meta_description")
        val metaDescription: String? = "",

        @SerializedName("Type")
        val type: String? = "",

        @SerializedName("search_applink")
        val searchApplink: String? = "",

        @SerializedName("Identifier")
        val identifier: String? = "",

        @SerializedName("search_url")
        val searchUrl: String? = "",

        @SerializedName("Id")
        val id: Int = 0,

        @SerializedName("Tags")
        val tags: String? = ""
)