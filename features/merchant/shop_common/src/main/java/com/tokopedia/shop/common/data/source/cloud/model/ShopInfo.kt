package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class ShopInfo {
    @SerializedName("address")
    @Expose
    var address: List<ShopInfoAddress> = ArrayList()

    @SerializedName("closed_info")
    @Expose
    var closedInfo = ShopInfoClosedInfo()

    @SerializedName("info")
    @Expose
    var info = ShopInfoDetail()

    @SerializedName("is_open")
    @Expose
    var isOpen: Long = 0

    @SerializedName("owner")
    @Expose
    var owner = ShopInfoOwner()

    @SerializedName("payment")
    @Expose
    var payment: List<ShopInfoPayment> = ArrayList()

    @SerializedName("ratings")
    @Expose
    var ratings = ShopInfoRatings()

    @SerializedName("shipment")
    @Expose
    var shipment: List<ShopInfoShipment> = ArrayList()

    @SerializedName("shop_tx_stats")
    @Expose
    var shopTxStats = ShopInfoTxStats()

    @SerializedName("stats")
    @Expose
    var stats = ShopInfoStats()

    @SerializedName("use_ace")
    @Expose
    var useAce: Long = 0
}