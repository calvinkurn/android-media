package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel
import com.tokopedia.play_common.types.PlayChannelStatusType
import java.util.*

/**
 * Created by jegul on 01/10/21
 */
interface PlayBroadcastChannelRepository {

    suspend fun getBroadcastingConfig(authorID: String, authorType: String): BroadcastingConfigUIModel

    suspend fun getAccountList(): List<ContentAccountUiModel>

    suspend fun getChannelConfiguration(authorId: String, authorType: String): ConfigurationUiModel

    suspend fun createChannel(authorId: String, authorType: String): String

    suspend fun updateChannelStatus(authorId: String, channelId: String, status: PlayChannelStatusType): String

    suspend fun updateSchedule(
        channelId: String,
        selectedDate: Date?
    ): BroadcastScheduleUiModel

    fun canSchedule(): Boolean
}
