package com.tokopedia.shopdiscount.info.data.uimodel

import com.tokopedia.shopdiscount.common.data.response.ResponseHeader


data class ShopDiscountSellerInfoUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val isUseVps: Boolean = false,
    val listSlashPriceBenefitData: List<SlashPriceBenefitData> = listOf()
) {
    data class SlashPriceBenefitData(
        val packageId: String = "",
        val packageName: String = "",
        val remainingQuota: Int = -1,
        val maxQuota: Int = -1,
        val expiredAt: String = "",
        val expiredAtUnix: Long = 0L,
    )
}