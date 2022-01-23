package com.tokopedia.vouchercreation.product.create.domain.entity

data class BroadcastMetadata(
    val couponId : Int,
    val shopDomain: String,
    val shopName: String,
    val broadcastPromo: Int,
    val broadcastStatus: Int
)
