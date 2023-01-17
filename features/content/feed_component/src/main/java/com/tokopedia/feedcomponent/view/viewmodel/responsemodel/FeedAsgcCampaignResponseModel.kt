package com.tokopedia.feedcomponent.view.viewmodel.responsemodel

import com.tokopedia.feedcomponent.data.feedrevamp.FeedASGCUpcomingReminderStatus

data class FeedAsgcCampaignResponseModel(
    var rowNumber : Int = 0,
    var campaignId: Long,
    var reminderStatus: FeedASGCUpcomingReminderStatus
)