package com.tokopedia.profilecommon.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UserProfileCompletionUpdatePojo(
        @SerializedName("userProfileUpdate") @Expose
        var data: UserProfileCompletionUpdate = UserProfileCompletionUpdate()
)

data class UserProfileCompletionUpdate(
        @SerializedName("isSuccess") @Expose
        var isSuccess: Boolean = false,
        @SerializedName("completionScore") @Expose
        var completionScore: Int = 0,
        @SerializedName("completionDone") @Expose
        var completionDone: Boolean = false,
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