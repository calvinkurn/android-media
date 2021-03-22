package com.tokopedia.seller.menu.common.utils

enum class FastingPeriod(val regularMerchantIllustrationUrl: String,
                         val powerMerchantIllustrationUrl: String,
                         val officialStoreIllustrationUrl: String) {
    // TODO: Set illustration URLs
    SUHOOR("", "", ""),
    FASTING("", "", ""),
    IFTAR("", "", "")
}