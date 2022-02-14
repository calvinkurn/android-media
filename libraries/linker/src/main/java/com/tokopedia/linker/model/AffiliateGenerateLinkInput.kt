package com.tokopedia.linker.model

import com.google.gson.annotations.SerializedName

data class AdditionalParam (
    @SerializedName("Key")
    var key: String? = "",

    @SerializedName("Value")
    var value: String? = ""
)

data class Link (
    @SerializedName("Type")
    var type: String? = "",

    @SerializedName("URL")
    var uRL: String? = "",

    @SerializedName("Identifier")
    var identifier: String? = "",

    @SerializedName("IdentifierType")
    var identifierType: Int = 0,

    @SerializedName("AdditionalParams")
    var additionalParams: ArrayList<AdditionalParam>? = null
)

data class AffiliateGenerateLinkInput (
    @SerializedName("source")
    var source: String? = "",

    @SerializedName("channel")
    var channel: ArrayList<Int>? = null,

    @SerializedName("link")
    var link: ArrayList<Link>? = null
)


