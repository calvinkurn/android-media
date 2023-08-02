package com.tokopedia.loginregister.shopcreation.data

import com.google.gson.annotations.SerializedName

data class GetUserProfileCompletionPojo(
    @SerializedName("userProfileCompletion")
    var data: UserProfileCompletionData = UserProfileCompletionData()
)

data class UserProfileCompletionData(
    @SerializedName("isActive")
    var isActive: Boolean = false,
    @SerializedName("fullName")
    var fullName: String = "",
    @SerializedName("birthDate")
    var birthDate: String = "",
    @SerializedName("birthDay")
    var birthDay: String = "",
    @SerializedName("birthMonth")
    var birthMonth: String = "",
    @SerializedName("birthYear")
    var birthYear: String = "",
    @SerializedName("gender")
    var gender: Int = 0,
    @SerializedName("email")
    var email: String = "",
    @SerializedName("msisdn")
    var phone: String = "",
    @SerializedName("isMsisdnVerified")
    var isPhoneVerified: Boolean = false,
    @SerializedName("isCreatedPassword")
    var isCreatedPassword: Boolean = false,
    @SerializedName("isBiodataDone")
    var isBiodataDone: Boolean = false,
    @SerializedName("isEmailDone")
    var isEmailDone: Boolean = false,
    @SerializedName("isMsisdnDone")
    var isMsisdnDone: Boolean = false,
    @SerializedName("completionScore")
    var completionScore: Int = 0,
    @SerializedName("completionDone")
    var completionDone: Boolean = false
)
