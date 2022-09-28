package com.tokopedia.shop.flashsale.domain.entity.enums

private const val FIRST_TAB_POSITION = 0
private const val SECOND_TAB_POSITION = 1

enum class CampaignListTab(val position: Int) {
    ACTIVE_CAMPAIGN(FIRST_TAB_POSITION),
    HISTORY_CAMPAIGN(SECOND_TAB_POSITION)
}