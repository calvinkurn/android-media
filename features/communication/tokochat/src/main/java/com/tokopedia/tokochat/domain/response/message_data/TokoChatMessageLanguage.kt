package com.tokopedia.tokochat.domain.response.message_data

import com.google.gson.annotations.SerializedName

data class TokoChatMessageLanguage(
    @SerializedName("en_ID")
    val enID: String? = "",

    @SerializedName("id_ID")
    val idID: String? = ""
)
