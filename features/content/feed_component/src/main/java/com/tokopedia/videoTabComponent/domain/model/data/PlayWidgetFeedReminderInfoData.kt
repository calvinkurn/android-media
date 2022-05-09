package com.tokopedia.videoTabComponent.domain.model.data

import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType

data class PlayWidgetFeedReminderInfoData (
    val channelId: String,
    val reminderType: PlayWidgetReminderType,
    val itemPosition: Int
)