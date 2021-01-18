package com.tokopedia.play.data

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 07/01/21.
 */
data class ProductTracking(
        @SerializedName("success")
        val success: Boolean = false,
) {

    data class Response(
            @SerializedName("broadcasterReportTrackViewer")
            val productTracking: ProductTracking = ProductTracking()
    )
}