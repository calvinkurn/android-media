package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.network.exception.MessageErrorException

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

data class PinnedProductException(override val message: String = ""): MessageErrorException()
