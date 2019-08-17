package com.tokopedia.profilecompletion.addphone.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-08-05.
 * ade.hadian@tokopedia.com
 */

data class UserValidatePojo(
        @SerializedName("userProfileCompletionValidate")
        @Expose
        var userProfileCompletionValidate: UserProfileCompletionValidate = UserProfileCompletionValidate()
)

data class UserProfileCompletionValidate(
        @SerializedName("isValid")
        @Expose
        var isValid: Boolean = false,
        @SerializedName("msisdnMessage")
        @Expose
        var msisdnMessage: String = ""
)