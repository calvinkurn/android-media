package com.tokopedia.campaign.data.request

import com.google.gson.annotations.SerializedName

data class GetRollenceGradualRolloutRequest(
    @SerializedName("id")
    val id: String,

    @SerializedName("rev")
    val rev: Int? = 0,

    @SerializedName("client_id")
    val client_id: Int,

    @SerializedName("iris_session_id")
    val iris_session_id: String
)
