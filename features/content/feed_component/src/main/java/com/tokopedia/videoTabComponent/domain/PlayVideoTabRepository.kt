package com.tokopedia.videoTabComponent.domain

import com.tokopedia.play.widget.data.PlayWidgetReminder
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.videoTabComponent.domain.model.data.ContentSlotResponse
import com.tokopedia.videoTabComponent.domain.model.data.VideoPageParams

interface PlayVideoTabRepository {
    suspend fun getPlayData(params: VideoPageParams): ContentSlotResponse
    suspend fun getPlayDetailPageResult(cursor: String, sourceId: String, sourceType: String, group: String): ContentSlotResponse
    suspend fun updateToggleReminder(channelId: String, reminderType: PlayWidgetReminderType): PlayWidgetReminder
}
