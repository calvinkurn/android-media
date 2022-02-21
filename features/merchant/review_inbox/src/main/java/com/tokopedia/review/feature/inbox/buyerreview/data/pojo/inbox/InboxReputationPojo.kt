package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReputationPojo(
    @SerializedName("inbox_reputation")
    @Expose
    val inboxReputation: List<InboxReputation> = listOf(),

    @SerializedName("paging")
    @Expose
    val paging: Paging = Paging()
)