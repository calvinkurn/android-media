package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReputation(
    @SerializedName("inbox_id")
    @Expose
    val inboxId: Long = 0,

    @SerializedName("shop_id")
    @Expose
    val shopId: Long = 0,

    @SerializedName("user_id")
    @Expose
    val userId: Long = 0,

    @SerializedName("reputation_id")
    @Expose
    val reputationId: Long = 0,

    @SerializedName("order_data")
    @Expose
    val orderData: OrderData = OrderData(),

    @SerializedName("reviewee_data")
    @Expose
    val revieweeData: RevieweeData = RevieweeData(),

    @SerializedName("reputation_data")
    @Expose
    val reputationData: ReputationData = ReputationData()
)