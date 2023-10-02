package com.tokopedia.tkpd.flashsale.domain.entity

data class TabMetadata(
    val tabs: List<Tab>
) {
    data class Tab(
        val tabId: Int,
        val tabName: String,
        val totalFlashSaleCount: Int,
        val displayName: String
    )
}
