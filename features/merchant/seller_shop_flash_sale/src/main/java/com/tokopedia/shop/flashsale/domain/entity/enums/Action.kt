package com.tokopedia.shop.flashsale.domain.entity.enums


private const val CAMPAIGN_ACTION_CREATE = 0
private const val CAMPAIGN_ACTION_UPDATE = 1
private const val CAMPAIGN_ACTION_SUBMIT = 2

enum class Action(val id : Int) {
    CREATE(CAMPAIGN_ACTION_CREATE),
    UPDATE(CAMPAIGN_ACTION_UPDATE),
    SUBMIT(CAMPAIGN_ACTION_SUBMIT)
}