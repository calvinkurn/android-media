package com.tokopedia.shop.flash_sale.domain.entity

data class ProductListMeta(
    val id: Int,
    val totalCampaign: Int,
    val name: String,
    val status: List<Int>
)
