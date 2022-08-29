package com.tokopedia.content.common.onboarding.model

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
data class FeedProfileValidateUsernameResponse(
    @SerializedName("feedXProfileValidateUsername")
    val wrapper: Wrapper = Wrapper(),
) {

    data class Wrapper(
        @SuppressLint("Invalid Data Type")
        @SerializedName("isValid")
        val isValid: Boolean = false,

        @SerializedName("notValidInformation")
        val notValidInformation: String = "",
    )
}