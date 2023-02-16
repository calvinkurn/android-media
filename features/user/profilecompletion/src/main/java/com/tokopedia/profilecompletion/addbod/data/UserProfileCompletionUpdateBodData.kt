package com.tokopedia.profilecompletion.addbod.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

data class AddBodData(
    @SerializedName("isSuccess")
    var isSuccess: Boolean = false,
    @SerializedName("birthDateMessage")
    var birthDateMessage: String = "",
    @SerializedName("completionScore")
    var completionScore: Int = 0
)

data class UserProfileCompletionUpdateBodData(
    @SerializedName("userProfileCompletionUpdate")
    var addBodData: AddBodData = AddBodData()
)
