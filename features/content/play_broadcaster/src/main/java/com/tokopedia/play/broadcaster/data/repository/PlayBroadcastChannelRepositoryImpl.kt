package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.teleporter.Teleporter.gson
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase
import com.tokopedia.content.common.usecase.GetWhiteListNewUseCase.Companion.WHITELIST_ENTRY_POINT
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.play.broadcaster.domain.model.Config
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.usecase.CreateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetConfigurationUseCase
import com.tokopedia.play.broadcaster.domain.usecase.config.GetBroadcastingConfigurationUseCase
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_RFC3339
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.remoteconfig.RemoteConfig
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 01/10/21
 */
class PlayBroadcastChannelRepositoryImpl @Inject constructor(
    private val getBroadcastingConfig: GetBroadcastingConfigurationUseCase,
    private val getConfigurationUseCase: GetConfigurationUseCase,
    private val createChannelUseCase: CreateChannelUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val remoteConfig: RemoteConfig,
    private val mapper: PlayBroadcastMapper,
    private val dispatchers: CoroutineDispatchers,
    private val getWhiteListNewUseCase: GetWhiteListNewUseCase,
) : PlayBroadcastChannelRepository {

    override suspend fun getBroadcastingConfig(
        authorID: String,
        authorType: String
    ): BroadcastingConfigUIModel = withContext(dispatchers.io) {
        val request = getBroadcastingConfig.execute(authorID, authorType)
        return@withContext mapper.mapBroadcastingConfig(request)
    }

    override suspend fun getAccountList(): List<ContentAccountUiModel> =
        withContext(dispatchers.io) {
            val response = getWhiteListNewUseCase.execute(type = WHITELIST_ENTRY_POINT)

            return@withContext mapper.mapAuthorList(response)
        }

    override suspend fun getChannelConfiguration(
        authorId: String,
        authorType: String
    ): ConfigurationUiModel = withContext(dispatchers.io) {
        val response = getConfigurationUseCase.execute(authorId = authorId, authorType = authorType)

        return@withContext mapper.mapConfiguration(mapConfiguration(response.authorConfig.config)
            .copy(
                streamAllowed = response.authorConfig.streamAllowed,
                shortVideoAllowed = response.authorConfig.shortVideoAllowed,
                tnc = response.authorConfig.tnc
            )
        )
    }

    private fun mapConfiguration(config: String): Config {
        return try {
            gson.fromJson(config, Config::class.java)
        } catch (e: Exception) {
            Config()
        }
    }

    override suspend fun createChannel(authorId: String, authorType: String): String = withContext(dispatchers.io) {
        val response = createChannelUseCase.apply {
            params = CreateChannelUseCase.createParams(
                authorId = authorId,
                authorType = authorType,
                type = CreateChannelUseCase.Type.Livestream,
            )
        }.executeOnBackground()
        return@withContext response.id
    }

    override suspend fun updateChannelStatus(
        authorId: String,
        channelId: String,
        status: PlayChannelStatusType
    ): String = withContext(dispatchers.io) {
        val response = updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(
                    channelId = channelId,
                    authorId = authorId,
                    status = status
                )
            )
        }.executeOnBackground()
        return@withContext response.id
    }

    override suspend fun updateSchedule(
        channelId: String,
        selectedDate: Date?
    ): BroadcastScheduleUiModel {
        return if (selectedDate == null) deleteSchedule(channelId)
        else setSchedule(channelId, selectedDate)
    }

    override fun canSchedule(): Boolean {
        return remoteConfig.getBoolean(KEY_ENABLE_SCHEDULING, true)
    }

    private suspend fun setSchedule(
        channelId: String,
        selectedDate: Date
    ): BroadcastScheduleUiModel = withContext(dispatchers.io) {
        val formattedDate = selectedDate.toFormattedString(DATE_FORMAT_RFC3339)

        updateChannelUseCase.apply {
            setQueryParams(
                PlayBroadcastUpdateChannelUseCase.createUpdateBroadcastScheduleRequest(
                    channelId = channelId,
                    status = PlayChannelStatusType.ScheduledLive,
                    date = formattedDate
                )
            )
        }.executeOnBackground()

        return@withContext BroadcastScheduleUiModel.Scheduled(
            time = selectedDate,
            formattedTime = formattedDate
        )
    }

    private suspend fun deleteSchedule(channelId: String) = withContext(dispatchers.io) {
        updateChannelUseCase.apply {
            setQueryParams(
                PlayBroadcastUpdateChannelUseCase.createDeleteBroadcastScheduleRequest(
                    channelId = channelId
                )
            )
        }.executeOnBackground()

        return@withContext BroadcastScheduleUiModel.NoSchedule
    }

    companion object {
        private const val KEY_ENABLE_SCHEDULING = "android_main_app_enable_play_scheduling"
    }
}
