package com.tokopedia.age_restriction.data


import com.google.gson.annotations.SerializedName

data class UserDOBUpdateData(
        @SerializedName("age")
        var age: Int,
        @SerializedName("bday")
        var bday: String,
        @SerializedName("error")
        var error: String,
        @SerializedName("isAdult")
        var isAdult: Boolean,
        @SerializedName("isDobExist")
        var isDobExist: Boolean,
        @SerializedName("isDobVerified")
        var isDobVerified: Boolean,
        @SerializedName("isSuccess")
        var isSuccess: Int,
        @SerializedName("userID")
        var userID: Int
)