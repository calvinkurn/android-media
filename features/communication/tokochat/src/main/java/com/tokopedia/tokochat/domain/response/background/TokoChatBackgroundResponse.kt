package com.tokopedia.tokochat.domain.response.background

import com.google.gson.annotations.SerializedName

data class TokoChatBackgroundResponse(
        @SerializedName("chatBackground")
        val tokoChatBackground: TokoChatBackground = TokoChatBackground()
)
