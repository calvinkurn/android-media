package com.tokopedia.loginregister.shopcreation.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetUserProfileCompletionPojo(
        @SerializedName("userProfileCompletion")
        @Expose
        var data: UserProfileCompletionData = UserProfileCompletionData()
)

data class UserProfileCompletionData(
        @SerializedName("isActive") @Expose
        var isActive: Boolean = false,
        @SerializedName("fullName") @Expose
        var fullName: String = "",
        @SerializedName("birthDate") @Expose
        var birthDate: String = "",
        @SerializedName("birthDay") @Expose
        var birthDay: String = "",
        @SerializedName("birthMonth") @Expose
        var birthMonth: String = "",
        @SerializedName("birthYear") @Expose
        var birthYear: String = "",
        @SerializedName("gender") @Expose
        var gender: Int = 0,
        @SerializedName("email") @Expose
        var email: String = "",
        @SerializedName("msisdn") @Expose
        var phone: String = "",
        @SerializedName("isMsisdnVerified") @Expose
        var isPhoneVerified: Boolean = false,
        @SerializedName("isCreatedPassword") @Expose
        var isCreatedPassword: Boolean = false,
        @SerializedName("isBiodataDone") @Expose
        var isBiodataDone: Boolean = false,
        @SerializedName("isEmailDone") @Expose
        var isEmailDone: Boolean = false,
        @SerializedName("isMsisdnDone") @Expose
        var isMsisdnDone: Boolean = false,
        @SerializedName("completionScore") @Expose
        var completionScore: Int = 0,
        @SerializedName("completionDone") @Expose
        var completionDone: Boolean = false
)