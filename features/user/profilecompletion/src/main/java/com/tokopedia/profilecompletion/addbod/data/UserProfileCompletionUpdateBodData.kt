package com.tokopedia.profilecompletion.addbod.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

data class UserProfileCompletionUpdateBodData(
    @SerializedName("userProfileCompletionUpdate")
    var addBodData: AddBodData = AddBodData()
)