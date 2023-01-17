package com.tokopedia.feedcomponent.view.viewmodel.responsemodel

import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard

data class FeedWidgetData (
    var rowNumber: Int = 0,
    val feedXCard: FeedXCard
)