package com.tokopedia.shop.flash_sale.domain.entity

data class TabMeta(
    val id: Int,
    val totalCampaign: Int,
    val name: String,
    val status: List<Int>
)
