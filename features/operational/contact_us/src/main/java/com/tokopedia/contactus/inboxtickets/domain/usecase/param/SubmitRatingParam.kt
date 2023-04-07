package com.tokopedia.contactus.inboxtickets.domain.usecase.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SubmitRatingParam(
    @SerializedName("commentID")
    val commentID: String = "",
    @SerializedName("rating")
    val rating: Int = 0,
    @SerializedName("reason")
    val reason: String = "",
) : GqlParam
