package com.tokopedia.contactus.inboxtickets.domain.usecase.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetTicketListParam(
    @SerializedName("userID")
    val userID: String = "",
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("rating")
    val rating: Long? = null
) : GqlParam
