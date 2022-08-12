package com.tokopedia.profilecompletion.profilecompletion.data

import com.google.gson.annotations.SerializedName

data class SaveProfilePictureResponse(
    @SerializedName("saveProfilePicture")
    var data: SaveProfilePictureData = SaveProfilePictureData()
)

data class SaveProfilePictureData(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("errorMessage")
    var errorMessage: List<String> = listOf(),
    @SerializedName("data")
    var innerData: SaveProfilePictureInnerData = SaveProfilePictureInnerData()
)

data class SaveProfilePictureInnerData(
    @SerializedName("imageURL")
    var imageUrl: String = "",
    @SerializedName("isSuccess")
    var isSuccess: Int = 0
)