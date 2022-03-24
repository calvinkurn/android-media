package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import java.util.*

/**
 * Created by jegul on 01/10/21
 */
interface PlayBroadcastChannelRepository {

    suspend fun getChannelConfiguration(): ConfigurationUiModel

    suspend fun createChannel(): String

    suspend fun updateChannelStatus(channelId: String, status: PlayChannelStatusType): String

    suspend fun updateSchedule(
        channelId: String,
        selectedDate: Date?
    ): BroadcastScheduleUiModel
}