package com.tokopedia.age_restriction.data

import com.google.gson.annotations.SerializedName

data class UserDOBUpdateResponse(
        @SerializedName("userProfileDobUpdate")
        var userDobUpdateData: UserDOBUpdateData
)