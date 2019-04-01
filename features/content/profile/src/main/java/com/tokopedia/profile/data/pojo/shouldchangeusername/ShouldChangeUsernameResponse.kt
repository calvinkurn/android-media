package com.tokopedia.profile.data.pojo.shouldchangeusername

import com.google.gson.annotations.SerializedName

data class ShouldChangeUsernameResponse(
        @SerializedName("bymeIsUsernameChanged")
        val bymeIsUsernameChanged: BymeIsUsernameChanged = BymeIsUsernameChanged()
)