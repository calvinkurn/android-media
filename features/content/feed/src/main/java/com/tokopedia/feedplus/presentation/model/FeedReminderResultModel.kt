package com.tokopedia.feedplus.presentation.model

import com.tokopedia.feedplus.presentation.uiview.FeedCampaignRibbonType

/**
 * Created By : Muhammad Furqan on 19/05/23
 */
data class FeedReminderResultModel(
    val isSetReminder: Boolean,
    val reminderType: FeedCampaignRibbonType
)
