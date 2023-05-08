package com.tokopedia.mvc.domain.entity

data class GenerateVoucherImageMetadata(
    val voucherDetail: VoucherDetailData,
    val shopData: ShopData,
    val topSellingProductImageUrls: List<String>
)
