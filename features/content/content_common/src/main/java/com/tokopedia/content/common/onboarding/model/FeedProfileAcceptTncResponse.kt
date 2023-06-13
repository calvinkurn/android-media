package com.tokopedia.content.common.onboarding.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
data class FeedProfileAcceptTncResponse(
    @SerializedName("feedXProfileAcceptTnC")
    val wrapper: Wrapper = Wrapper(),
) {

    data class Wrapper(
        @SerializedName("hasAcceptTnC")
        val hasAcceptTnC: Boolean = false,
    )
}