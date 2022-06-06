package com.tokopedia.shop.flash_sale.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val CAMPAIGN_STATUS_ID_DRAFT = 2
const val CAMPAIGN_STATUS_ID_AVAILABLE = 5
const val CAMPAIGN_STATUS_ID_UPCOMING = 14
const val CAMPAIGN_STATUS_ID_ONGOING = 7

const val CAMPAIGN_STATUS_ID_FINISHED = 8
const val CAMPAIGN_STATUS_ID_CANCELLED = 13

@Parcelize
enum class CampaignStatus(val id: Int) : Parcelable {
    DRAFT(CAMPAIGN_STATUS_ID_DRAFT),
    AVAILABLE(CAMPAIGN_STATUS_ID_AVAILABLE),
    UPCOMING(CAMPAIGN_STATUS_ID_UPCOMING),
    ONGOING(CAMPAIGN_STATUS_ID_ONGOING),
    FINISHED(CAMPAIGN_STATUS_ID_FINISHED),
    CANCELLED(CAMPAIGN_STATUS_ID_CANCELLED)
}