package com.tokopedia.contactus.inboxtickets.domain.usecase.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class PostMessage2Param(
    @SerializedName("userID")
    val userID: String = "",
    @SerializedName("fileUploaded")
    val fileUploaded: String = "",
    @SerializedName("postKey")
    val postKey: String = "",
    @SerializedName("ticketID")
    val ticketID: String = "",
) : GqlParam
