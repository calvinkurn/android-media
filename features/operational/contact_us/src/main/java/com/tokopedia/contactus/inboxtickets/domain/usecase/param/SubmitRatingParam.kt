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
    @SerializedName("otherReason")
    val otherReason: String? = "",
    @SerializedName("service")
    val service: String? = "",
    @SerializedName("dynamicReasons")
    val dynamicReasons: List<String>? = emptyList()
) : GqlParam
