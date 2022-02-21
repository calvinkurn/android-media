package com.tokopedia.affiliate.model.response


import com.google.gson.annotations.SerializedName

data class AffiliateGenerateLinkData(
        @SerializedName("generateAffiliateLink")
        var affiliateGenerateLink: AffiliateGenerateLink
) {
    data class AffiliateGenerateLink(
            @SerializedName("Data")
            var `data`: List<Data>?
    ) {
        data class Data(
                @SerializedName("Status")
                var status: Int?,
                @SerializedName("Error")
                var error: String?,
                @SerializedName("Identifier")
                var identifier: String?,
                @SerializedName("IdentifierType")
                var identifierType: Int?,
                @SerializedName("URL")
                var url: Url?,
                @SerializedName("LinkID")
                var linkID : String?
        ) {
            data class Url(
                    @SerializedName("RegularURL")
                    var regularURL: String?,
                    @SerializedName("ShortURL")
                    var shortURL: String?
            )
        }
    }
}