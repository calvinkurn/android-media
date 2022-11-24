package com.tokopedia.mvc.domain.entity

data class VoucherCreationQuota (
    val used: Int = 0,
    val remaining: Int = 0,
    val total: Int = 0,
    val statusSource: String = "",
    val sources: List<Sources> = emptyList(),
    val tickerTitle: String = "",
    val ctaText: String = "",
    val ctaLink: String = "",
    val quotaUsageFormatted: String = "",
    val quotaErrorMessage: String = ""
) {
    data class Sources (
        val name: String = "",
        val used: Int = 0,
        val total: Int = 0,
        val expired: String = ""
    )
}
