package com.tokopedia.profilecommon.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UserProfileCompletionValidatePojo(
        @SerializedName("userProfileCompletionValidate") @Expose
        var data: UserProfileCompletionValidate = UserProfileCompletionValidate()
)

data class UserProfileCompletionValidate(
        @SerializedName("isDirectEdit") @Expose
        var isDirectEdit: Boolean = false,
        @SerializedName("isValid") @Expose
        var isValid: Boolean = false,
        @SerializedName("fullNameMessage") @Expose
        var fullNameMessage: String = "",
        @SerializedName("genderMessage") @Expose
        var genderMessage: String = "",
        @SerializedName("birthDateMessage") @Expose
        var birthDateMessage: String = "",
        @SerializedName("passwordMessage") @Expose
        var passwordMessage: String = "",
        @SerializedName("passwordConfirmMessage") @Expose
        var passwordConfirmMessage: String = "",
        @SerializedName("emailMessage") @Expose
        var emailMessage: String = "",
        @SerializedName("msisdnMessage") @Expose
        var msisdnMessage: String = ""
)