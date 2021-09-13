package com.tokopedia.review.feature.inbox.buyerreview.data.pojo.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class InboxReputationDetailPojo {
    @SerializedName("reputation_id")
    @Expose
    var reputationId: Long = 0

    @SerializedName("review_inbox_data")
    @Expose
    var reviewInboxData: List<ReviewInboxDatum>? = null

    @SerializedName("user_data")
    @Expose
    var userData: UserData? = null

    @SerializedName("shop_data")
    @Expose
    var shopData: ShopData? = null

    @SerializedName("invoice_ref_num")
    @Expose
    var invoiceRefNum: String? = null

    @SerializedName("invoice_time")
    @Expose
    var invoiceTime: String? = null

    @SerializedName("order_id")
    @Expose
    var orderId: String? = null
}