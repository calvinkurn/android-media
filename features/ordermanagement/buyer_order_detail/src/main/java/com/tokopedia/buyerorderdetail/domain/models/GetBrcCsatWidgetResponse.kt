package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetBrcCsatWidgetResponse(
    @SerializedName("resolution_get_csat_form_v4")
    @Expose
    val resolutionGetCsatFormV4: ResolutionGetCsatFormV4? = null
) {
    data class ResolutionGetCsatFormV4(
        @SerializedName("data")
        @Expose
        val data: Data? = null
    ) {
        data class Data(
            @SerializedName("isEligible")
            @Expose
            val isEligible: Boolean? = null,
            @SerializedName("url")
            @Expose
            val url: Url? = null
        ) {
            data class Url(
                @SerializedName("helpPage")
                @Expose
                val helpPage: HelpPage? = null
            ) {
                data class HelpPage(
                    @SerializedName("android")
                    @Expose
                    val android: String? = null
                )
            }
        }
    }
}
