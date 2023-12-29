package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
data class GetProfileSettingsResponse(
    @SerializedName("feedXProfileGetProfileSettings")
    val data: Data = Data(),
) {
    data class Data(
        @SerializedName("settingsProfile")
        val settingsProfile: List<SettingsProfile> = emptyList(),
    )

    data class SettingsProfile(
        @SerializedName("settingID")
        val settingID: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("enabled")
        val enabled: Boolean = false,
    )
}
