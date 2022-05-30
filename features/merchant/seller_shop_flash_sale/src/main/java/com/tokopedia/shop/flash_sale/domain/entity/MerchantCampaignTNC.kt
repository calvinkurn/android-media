package com.tokopedia.shop.flash_sale.domain.entity

data class MerchantCampaignTNC(
    val title: String = "",
    val messages: List<String> = listOf(),
    val error: Error = Error()
) {
    data class Error(
        val error_code: Int = 0,
        val error_message: String = "",
    )
}

