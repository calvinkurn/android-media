package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetBrcCsatWidgetResponse(
    @SerializedName("data")
    @Expose
    val data: Data? = null
) {
    data class Data(
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
                val isEligible: Boolean? = null
            )
        }
    }
}
