package com.tokopedia.sellerhome.settings.util

enum class FastingPeriod(val regularMerchantIllustrationUrl: String,
                         val powerMerchantIllustrationUrl: String,
                         val officialStoreIllustrationUrl: String) {
    // TODO: Set illustration URLs
    SUHOOR("", "", ""),
    FASTING("", "", ""),
    IFTAR("", "", "")
}