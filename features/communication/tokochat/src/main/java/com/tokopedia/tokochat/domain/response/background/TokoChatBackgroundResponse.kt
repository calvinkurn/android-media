package com.tokopedia.tokochat.domain.response.background

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokochat.domain.response.background.TokoChatBackground

data class TokoChatBackgroundResponse(
        @SerializedName("chatBackground")
        @Expose
        val tokoChatBackground: TokoChatBackground = TokoChatBackground()
)
