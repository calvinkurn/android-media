package com.tokopedia.shop.flash_sale.domain.entity.enums

private const val CAMPAIGN_STATUS_ID_SCHEDULED = 1
private const val CAMPAIGN_STATUS_ID_DRAFT = 2
private const val CAMPAIGN_STATUS_ID_PUBLISHED = 3
private const val CAMPAIGN_STATUS_ID_FINISHED = 8

enum class CampaignStatus(val id : Int) {
    SCHEDULED(CAMPAIGN_STATUS_ID_SCHEDULED),
    DRAFT(CAMPAIGN_STATUS_ID_DRAFT),
    PUBLISHED(CAMPAIGN_STATUS_ID_PUBLISHED),
    FINISHED(CAMPAIGN_STATUS_ID_FINISHED),
}