package com.tokopedia.shop.flashsale.domain.entity.enums

private const val CAMPAIGN_TYPE_FLASH_SALE_BY_SELLER = 0
private const val CAMPAIGN_TYPE_RILISAN_SPESIAL = 1

enum class CampaignType(val id : Int) {
    FLASH_SALE(CAMPAIGN_TYPE_FLASH_SALE_BY_SELLER),
    RILISAN_SPESIAL(CAMPAIGN_TYPE_RILISAN_SPESIAL),
}