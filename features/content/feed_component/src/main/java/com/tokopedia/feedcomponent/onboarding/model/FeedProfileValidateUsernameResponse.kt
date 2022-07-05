package com.tokopedia.feedcomponent.onboarding.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
data class FeedProfileValidateUsernameResponse(
    @SerializedName("feedXProfileValidateUsername")
    val wrapper: Wrapper = Wrapper(),
) {

    data class Wrapper(
        @SerializedName("isValid")
        val isValid: Long = 0,

        @SerializedName("notValidInformation")
        val notValidInformation: String = "",
    )
}