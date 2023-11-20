package com.tokopedia.affiliate.model.request

import com.google.gson.annotations.SerializedName

data class GenerateLinkRequest(
    @SerializedName("input")
    val input: Input
) {
    data class Input(
        @SerializedName("source")
        val source: String,
        @SerializedName("channel")
        val channel: List<Int?>,
        @SerializedName("pageType")
        val pageType: String,
        @SerializedName("itemID")
        val itemID: String,
        @SerializedName("link")
        val link: List<Link>
    ) {
        data class Link(
            @SerializedName("Type")
            val type: String,
            @SerializedName("URL")
            val url: String,
            @SerializedName("Identifier")
            val identifier: String,
            @SerializedName("IdentifierType")
            val identifierType: Int
        )
    }
}
