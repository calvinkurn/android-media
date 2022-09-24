package com.tokopedia.profilecompletion.profilecompletion.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-07-03.
 * ade.hadian@tokopedia.com
 */

data class UserProfileInfoData(
    @SerializedName("userProfileCompletion")
    var profileCompletionData: ProfileCompletionData = ProfileCompletionData()
)