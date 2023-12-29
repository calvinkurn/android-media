package com.tokopedia.loginregister.shopcreation.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

data class UserProfileUpdatePojo(
    @SerializedName("userProfileUpdate")
    var data: UserProfileUpdate = UserProfileUpdate()
)

data class UserProfileUpdate(
    @SerializedName("isSuccess")
    var isSuccess: Int = 0,
    @SerializedName("completionScore")
    var completionScore: Int = 0,
    @SerializedName("errors")
    var errors: List<String> = arrayListOf()
)
