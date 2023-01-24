package com.tokopedia.mvc.domain.entity

data class ShareComponentMetaData(
    val voucher: Voucher,
    val shopData: ShopData,
    val topSellingProductImageUrls: List<String>
)
