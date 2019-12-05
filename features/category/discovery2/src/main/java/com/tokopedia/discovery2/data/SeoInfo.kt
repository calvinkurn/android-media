package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName

data class SeoInfo(

        @SerializedName("amp_url_mobile")
        val ampUrlMobile: String? = "",

        @SerializedName("twitter_desc")
        val twitterDesc: String? = "",

        @SerializedName("meta_title")
        val metaTitle: String? = "",

        @SerializedName("og_title")
        val ogTitle: String? = "",

        @SerializedName("amp_url")
        val ampUrl: String? = "",

        @SerializedName("canonical_url")
        val canonicalUrl: String? = "",

        @SerializedName("og_image")
        val ogImage: String? = "",

        @SerializedName("robot")
        val robot: String? = "",

        @SerializedName("twitter_image")
        val twitterImage: String? = "",

        @SerializedName("meta_desc")
        val metaDesc: String? = "",

        @SerializedName("og_desc")
        val ogDesc: String? = "",

        @SerializedName("canonical_url_mobile")
        val canonicalUrlMobile: String? = "",

        @SerializedName("twitter_title")
        val twitterTitle: String? = ""
)