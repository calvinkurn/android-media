package com.tokopedia.contactus.inboxtickets.domain.usecase.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class PostMessageParam(
    @SerializedName("ticketID")
    val ticketID: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("pPhoto")
    val pPhoto: Int = 0,
    @SerializedName("pPhotoAll")
    val pPhotoAll: String = "",
    @SerializedName("userID")
    val userID: String = "",
    @SerializedName("agentReply")
    val agentReply: String = "",
) : GqlParam
