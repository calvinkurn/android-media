package com.tokopedia.profilecompletion.profilecompletion.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-07-08.
 * ade.hadian@tokopedia.com
 */

data class ProfileCompletionData(
    @SerializedName("isActive") var isActive: Boolean = false,
    @SerializedName("fullName") var fullName: String = "",
    @SerializedName("birthDate") var birthDate: String = "",
    @SerializedName("birthDay") var birthDay: String = "",
    @SerializedName("birthMonth") var birthMonth: String = "",
    @SerializedName("birthYear") var birthYear: String = "",
    @SerializedName("gender") var gender: Int = 0,
    @SerializedName("email") var email: String = "",
    @SerializedName("msisdn") var msisdn: String = "",
    @SerializedName("isMsisdnVerified") var isMsisdnVerified: Boolean = false,
    @SerializedName("isCreatedPassword") var isCreatedPassword: Boolean = false,
    @SerializedName("isBiodataDone") var isBiodataDone: Boolean = false,
    @SerializedName("isEmailDone") var isEmailDone: Boolean = false,
    @SerializedName("isPasswordDone") var isPasswordDone: Boolean = false,
    @SerializedName("isMsisdnDone") var isMsisdnDone: Boolean = false,
    @SerializedName("completionDone") var completionDone: Boolean = false,
    @SerializedName("completionScore") var completionScore: Int = 0,
    var profilePicture: String = ""
)