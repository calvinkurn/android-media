package com.tokopedia.shop.flash_sale.domain.entity

data class ShopInfo(
    val shopTierId: Int,
    val shopTier: String,
    val shopGradeId: Int,
    val shopGrade: String,
    val isPowerMerchant : Boolean,
    val isOfficial : Boolean
)
