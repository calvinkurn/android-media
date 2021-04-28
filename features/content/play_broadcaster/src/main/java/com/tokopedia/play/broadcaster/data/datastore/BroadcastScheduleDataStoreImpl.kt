package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_BROADCAST_SCHEDULE
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_RFC3339
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class BroadcastScheduleDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatchers,
        private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase
): BroadcastScheduleDataStore {

    private val _scheduleLiveData = MutableLiveData<BroadcastScheduleUiModel>()

    override fun getObservableSchedule(): LiveData<BroadcastScheduleUiModel> {
        return _scheduleLiveData
    }

    override fun setBroadcastSchedule(scheduleDate: BroadcastScheduleUiModel) {
        _scheduleLiveData.value = scheduleDate
    }

    override fun getSchedule(): BroadcastScheduleUiModel? {
        return _scheduleLiveData.value
    }

    override suspend fun updateBroadcastSchedule(channelId: String, scheduledTime: Date): NetworkResult<Unit> {
        return try {
            val schedule = BroadcastScheduleUiModel.Scheduled(
                    time = scheduledTime,
                    formattedTime = scheduledTime.toFormattedString(DATE_FORMAT_BROADCAST_SCHEDULE, Locale("id", "ID"))
            )
            updateSchedule(
                    channelId = channelId,
                    schedule = schedule
            )
            setBroadcastSchedule(schedule)
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    override suspend fun deleteBroadcastSchedule(channelId: String): NetworkResult<Unit> {
        return try {
            val schedule = BroadcastScheduleUiModel.NoSchedule
            updateSchedule(
                    channelId = channelId,
                    schedule = schedule
            )
            setBroadcastSchedule(schedule)
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun updateSchedule(channelId: String, schedule: BroadcastScheduleUiModel) = withContext(dispatcher.io) {
        when (schedule) {
            is BroadcastScheduleUiModel.Scheduled -> addEditSchedule(channelId, schedule)
            BroadcastScheduleUiModel.NoSchedule -> removeSchedule(channelId)
        }
    }

    private suspend fun addEditSchedule(channelId: String, schedule: BroadcastScheduleUiModel.Scheduled) = withContext(dispatcher.io) {
        val selectedDate = schedule.time

        updateChannelUseCase.apply {
            setQueryParams(
                    PlayBroadcastUpdateChannelUseCase.createUpdateBroadcastScheduleRequest(
                            channelId = channelId,
                            status = PlayChannelStatusType.ScheduledLive,
                            date = selectedDate.toFormattedString(DATE_FORMAT_RFC3339)
                    )
            )
        }
        return@withContext updateChannelUseCase.executeOnBackground()
    }

    private suspend fun removeSchedule(channelId: String) = withContext(dispatcher.io) {
        updateChannelUseCase.apply {
            setQueryParams(
                    PlayBroadcastUpdateChannelUseCase.createDeleteBroadcastScheduleRequest(
                            channelId = channelId
                    )
            )
        }
        return@withContext updateChannelUseCase.executeOnBackground()
    }
}