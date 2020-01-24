package com.tokopedia.sellerhomedrawer.data.userdata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerhomedrawer.data.userdata.notifications.*


class Notifications {

    @SerializedName("sales")
    @Expose
    var sales: Sales? = null
    @SerializedName("inbox")
    @Expose
    val inbox: Inbox? = null
    @SerializedName("purchase")
    @Expose
    var purchase: Purchase? = null
    @SerializedName("resolution_as")
    @Expose
    val resolutionAs: ResolutionAs? = null
    @SerializedName("total_notif")
    @Expose
    val totalNotif: Int = 0
    @SerializedName("total_cart")
    @Expose
    val totalCart: Int = 0
    @SerializedName("resolution")
    @Expose
    val resolution: Int = 0
    @SerializedName("shop_id")
    @Expose
    val shopId: Int = 0
    @SerializedName("chat")
    @Expose
    val chat: Chat? = null
    @SerializedName("incr_notif")
    @Expose
    val incrNotif: Int = 0

}
