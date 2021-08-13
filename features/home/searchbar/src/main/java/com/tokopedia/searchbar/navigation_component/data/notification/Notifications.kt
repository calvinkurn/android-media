package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class Notifications(
    @SerializedName("buyerOrderStatus")
    val buyerOrderStatus: BuyerOrderStatus = BuyerOrderStatus(),
    @SerializedName("chat")
    val chat: Chat = Chat(),
    @SerializedName("inbox")
    val inbox: Inbox = Inbox(),
    @SerializedName("inbox_counter")
    val inboxCounter: InboxCounter = InboxCounter(),
    @SerializedName("resolutionAs")
    val resolutionAs: ResolutionAs = ResolutionAs(),
    @SerializedName("sellerInfo")
    val sellerInfo: SellerInfo = SellerInfo(),
    @SerializedName("sellerOrderStatus")
    val sellerOrderStatus: SellerOrderStatus = SellerOrderStatus(),
    @SerializedName("total_cart")
    val totalCart: Int = 0
)