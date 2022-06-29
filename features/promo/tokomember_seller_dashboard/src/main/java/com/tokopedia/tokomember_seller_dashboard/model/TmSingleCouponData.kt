package com.tokopedia.tokomember_seller_dashboard.model

data class TmSingleCouponData(
    var level: Int = 0, //0 for premium 1 for vip
    var typeCoupon: String = "", // 0 for cashback 1 for gratis ongkir
    var typeCashback: String = "", // 0 for Rp 1 for %
    var maxCashback: String = "",
    var minTransaki: String = "",
    var cashBackPercentage: Int = 0,
    var quota: String = "100",
    var imageUrl: String = ""
)
