package com.tokopedia.content.common.onboarding.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
data class FeedProfileSubmitResponse(
    @SerializedName("feedXProfileSubmit")
    val wrapper: Wrapper = Wrapper()
) {

    data class Wrapper(
        @SerializedName("status")
        val status: Boolean = false,
    )
}