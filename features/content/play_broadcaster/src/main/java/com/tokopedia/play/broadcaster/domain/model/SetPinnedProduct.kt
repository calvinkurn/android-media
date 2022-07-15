package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 13/07/22
 */
data class SetPinnedProduct(
    @SerializedName("broadcasterSetPinnedProductTag")
    val data: Response = Response(),
) {
    data class Response(
        @SerializedName("success")
        val success: Boolean = false,
    )
}
