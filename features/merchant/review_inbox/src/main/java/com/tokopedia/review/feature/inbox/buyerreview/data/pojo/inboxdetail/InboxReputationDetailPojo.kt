package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class InboxReputationDetailPojo(
    @SerializedName("reputation_id")
    @Expose
    val reputationId: Long = 0,

    @SerializedName("review_inbox_data")
    @Expose
    val reviewInboxData: List<ReviewInboxDatum> = listOf(),

    @SerializedName("user_data")
    @Expose
    val userData: UserData = UserData(),

    @SerializedName("shop_data")
    @Expose
    val shopData: ShopData = ShopData(),

    @SerializedName("invoice_ref_num")
    @Expose
    val invoiceRefNum: String = "",

    @SerializedName("invoice_time")
    @Expose
    val invoiceTime: String = "",

    @SerializedName("order_id")
    @Expose
    val orderId: String = ""
)