package com.tokopedia.linker.model

import com.google.gson.annotations.SerializedName

data class URL (
    @SerializedName("ShortURL")
    var shortURL: String? = "",

    @SerializedName("RegularURL")
    var regularURL: String? = ""
)

data class Data (
    @SerializedName("Status")
    var status: Int = 0,

    @SerializedName("Type")
    var type: String? = "",

    @SerializedName("Error")
    var error: String? = "",

    @SerializedName("Identifier")
    var identifier: String? = "",

    @SerializedName("IdentifierType")
    var identifierType: Int = 0,

    @SerializedName("LinkID")
    var linkID: Int = 0,

    @SerializedName("URL")
    var uRL: URL? = null
)

data class GenerateAffiliateLink (
    @SerializedName("Data")
    var data: ArrayList<Data>? = null
){
    data class Response(
        @SerializedName("generateAffiliateLink")
        val generateAffiliateLink: GenerateAffiliateLink
    )
}