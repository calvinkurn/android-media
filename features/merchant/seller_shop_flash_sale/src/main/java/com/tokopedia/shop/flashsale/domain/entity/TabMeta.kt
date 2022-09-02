package com.tokopedia.shop.flashsale.domain.entity

data class TabMeta(
    val id: Int,
    val totalCampaign: Int,
    val name: String,
    val status: List<Int>
)
