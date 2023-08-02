package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
data class GetProfileSettingsRequest(
    @SerializedName("authorID")
    val authorID: String,

    @SerializedName("authorType")
    val authorType: Int,
)
