package com.tokopedia.loginregister.shopcreation.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UserProfileUpdatePojo(
        @SerializedName("userProfileUpdate") @Expose
        var data: UserProfileUpdate = UserProfileUpdate()
)

data class UserProfileUpdate(
        @SerializedName("isSuccess") @Expose
        var isSuccess: Int = 0,
        @SerializedName("completionScore") @Expose
        var completionScore: Int = 0,
        @SerializedName("errors") @Expose
        var errors: List<String> = arrayListOf()
)