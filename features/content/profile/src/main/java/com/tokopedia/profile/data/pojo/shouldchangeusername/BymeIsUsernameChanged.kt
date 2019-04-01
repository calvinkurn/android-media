package com.tokopedia.profile.data.pojo.shouldchangeusername

import com.google.gson.annotations.SerializedName

data class BymeIsUsernameChanged(
        @SerializedName("state")
        val state: Boolean = false
)