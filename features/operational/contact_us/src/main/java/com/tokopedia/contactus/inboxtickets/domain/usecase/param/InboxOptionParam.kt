package com.tokopedia.contactus.inboxtickets.domain.usecase.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class InboxOptionParam(
    @SerializedName("caseID")
    val caseID: String = "",
): GqlParam
