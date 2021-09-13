package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InboxReputation {
    @SerializedName("inbox_id")
    @Expose
    var inboxId: Long = 0

    @SerializedName("shop_id")
    @Expose
    var shopId: Long = 0

    @SerializedName("user_id")
    @Expose
    var userId: Long = 0

    @SerializedName("reputation_id")
    @Expose
    var reputationId: Long = 0

    @SerializedName("order_data")
    @Expose
    var orderData: OrderData? = null

    @SerializedName("reviewee_data")
    @Expose
    var revieweeData: RevieweeData? = null

    @SerializedName("reputation_data")
    @Expose
    var reputationData: ReputationData? = null
}