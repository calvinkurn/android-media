package com.tokopedia.homenav.mainnav.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.homenav.mainnav.data.pojo.user.UserPojo

data class MainNavPojo(
        @SerializedName("id")
        @Expose
        val id: String = "",
        var userPojo: UserPojo = UserPojo()
)