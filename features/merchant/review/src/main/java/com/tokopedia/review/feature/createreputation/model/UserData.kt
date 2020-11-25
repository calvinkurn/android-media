package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userID")
    @Expose
    val userID: Int = 0,
    @SerializedName("userName")
    @Expose
    val userName: String = "",
    @SerializedName("userStatus")
    @Expose
    val userStatus: Int = 0
)