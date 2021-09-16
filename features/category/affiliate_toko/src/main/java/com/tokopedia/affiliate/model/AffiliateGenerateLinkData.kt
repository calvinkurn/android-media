package com.tokopedia.affiliate.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AffiliateGenerateLinkData(
        @SerializedName("generateAffiliateLink")
        var affiliateGenerateLink: AffiliateGenerateLink
) {
    @Keep
    data class AffiliateGenerateLink(
            @SerializedName("Data")
            var `data`: List<Data>
    ) {
        @Keep
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
                var url: Data.Url?
        ) {
            @Keep
            data class Url(
                    @SerializedName("RegularURL")
                    var regularURL: String,
                    @SerializedName("ShortURL")
                    var shortURL: String
            )
        }
    }
}