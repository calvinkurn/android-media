package com.tokopedia.contactus.inboxtickets.domain.usecase.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class CloseTicketByUserParam(
    @SerializedName("caseID")
    val caseID: String = "",
    @SerializedName("source")
    val source: String = "",
): GqlParam
