package com.tokopedia.tkpd.flashsale.domain.entity.enums

enum class UpcomingCampaignStatus {
    AVAILABLE,
    NO_PRODUCT_ELIGIBLE,
    FULL_QUOTA,
    CLOSED
}

fun UpcomingCampaignStatus.isFlashSaleAvailable(): Boolean {
    return this == UpcomingCampaignStatus.AVAILABLE
}
