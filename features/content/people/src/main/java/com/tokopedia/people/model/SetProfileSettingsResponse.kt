package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
data class SetProfileSettingsResponse(
    @SerializedName("feedXProfileSetProfileSettings")
    val data: Data = Data()
) {

    data class Data(
        @SerializedName("success")
        val success: Boolean = false,
    )
}
