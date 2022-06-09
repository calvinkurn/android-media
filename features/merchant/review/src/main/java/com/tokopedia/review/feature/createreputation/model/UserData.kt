package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserData(
    @SerializedName("userID")
    @Expose
    val userID: String = "0",
    @SerializedName("userName")
    @Expose
    val userName: String = "",
    @SerializedName("userStatus")
    @Expose
    val userStatus: Int = 0
) : Serializable