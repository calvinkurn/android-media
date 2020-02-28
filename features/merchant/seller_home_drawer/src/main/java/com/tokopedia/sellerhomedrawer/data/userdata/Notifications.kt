package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomedrawer.data.userdata.notifications.*


data class Notifications (

    @SerializedName("sales")
    @Expose
    var sales: Sales? = Sales(),
    @SerializedName("inbox")
    @Expose
    val inbox: Inbox? = Inbox(),
    @SerializedName("purchase")
    @Expose
    var purchase: Purchase? = Purchase(),
    @SerializedName("resolution_as")
    @Expose
    val resolutionAs: ResolutionAs? = ResolutionAs(),
    @SerializedName("total_notif")
    @Expose
    val totalNotif: Int = 0,
    @SerializedName("total_cart")
    @Expose
    val totalCart: Int = 0,
    @SerializedName("resolution")
    @Expose
    val resolution: Int = 0,
    @SerializedName("shop_id")
    @Expose
    val shopId: Int = 0,
    @SerializedName("chat")
    @Expose
    val chat: Chat? = Chat(),
    @SerializedName("incr_notif")
    @Expose
    val incrNotif: Int = 0

)

