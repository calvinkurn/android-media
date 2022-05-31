package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inbox

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReputation(
    @SerializedName("inbox_id")
    @Expose
    val inboxId: String = "",

    @SerializedName("shop_id")
    @Expose
    val shopId: String = "",

    @SerializedName("user_id")
    @Expose
    val userId: String = "",

    @SerializedName("reputation_id")
    @Expose
    val reputationId: String = "",

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