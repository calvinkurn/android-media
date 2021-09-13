package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InboxReputationPojo {
    @SerializedName("inbox_reputation")
    @Expose
    var inboxReputation: List<InboxReputation>? = null

    @SerializedName("paging")
    @Expose
    var paging: Paging? = null
}