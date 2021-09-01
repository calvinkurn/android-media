package com.tokopedia.exploreCategory.model


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AffiliateGenerateLinkData(
        @SerializedName("affiliateGenerateLink")
        var affiliateGenerateLink: AffiliateGenerateLink
) {
    @Keep
    data class AffiliateGenerateLink(
            @SerializedName("data")
            var `data`: Data
    ) {
        @Keep
        data class Data(
                @SerializedName("data")
                var `data`: List<Data>,
                @SerializedName("status")
                var status: Boolean
        ) {
            @Keep
            data class Data(
                    @SerializedName("channelID")
                    var channelID: Int,
                    @SerializedName("error")
                    var error: String,
                    @SerializedName("url")
                    var url: Url
            ) {
                @Keep
                data class Url(
                        @SerializedName("original")
                        var original: String,
                        @SerializedName("regular")
                        var regular: String,
                        @SerializedName("short")
                        var short: String
                )
            }
        }
    }
}