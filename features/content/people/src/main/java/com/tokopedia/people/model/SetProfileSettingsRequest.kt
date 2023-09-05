package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 10, 2023
 */
data class SetProfileSettingsRequest(
    @SerializedName("authorID")
    val authorID: String,

    @SerializedName("authorType")
    val authorType: Int,

    @SerializedName("data")
    val data: List<Data>,
) {

    data class Data(
        @SerializedName("settingID")
        val settingID: String,

        @SerializedName("enabled")
        val enabled: Boolean,
    )
}
