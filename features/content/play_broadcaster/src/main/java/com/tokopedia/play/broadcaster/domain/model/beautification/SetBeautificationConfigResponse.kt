package com.tokopedia.play.broadcaster.domain.model.beautification

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on March 07, 2023
 */
data class SetBeautificationConfigResponse(
    @SerializedName("broadcasterSetBeautificationConfig")
    val wrapper: Wrapper = Wrapper()
) {
    data class Wrapper(
        @SerializedName("success")
        val success: Boolean = false,
    )
}
