package com.tokopedia.age_restriction.data


import com.google.gson.annotations.SerializedName

data class UserDOBResponse(
        @SerializedName("age")
        var age: Int,
        @SerializedName("bday")
        var bday: String,
        @SerializedName("is_adult")
        var isAdult: Boolean,
        @SerializedName("is_dob_exist")
        var isDobExist: Boolean,
        @SerializedName("is_dob_verified")
        var isDobVerified: Boolean,
        @SerializedName("user_id")
        var userId: Int
)