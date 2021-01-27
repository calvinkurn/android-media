package com.tokopedia.top_ads_headline.data


import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error

data class TopadsManageHeadlineAdResponse(
    @SerializedName("data")
    var `data`: Data
) {
    data class Data(
            @SerializedName("topadsManageHeadlineAd")
            var topadsManageHeadlineAd: TopadsManageHeadlineAd
    ) {
        data class TopadsManageHeadlineAd(
                @SerializedName("data")
                var success: Success,
                @SerializedName("errors")
                var errors: List<Error>
        ) {
            data class Success(
                    @SerializedName("id")
                    var id: String,
                    @SerializedName("resourceURL")
                    var resourceURL: String
            )
        }
    }
}