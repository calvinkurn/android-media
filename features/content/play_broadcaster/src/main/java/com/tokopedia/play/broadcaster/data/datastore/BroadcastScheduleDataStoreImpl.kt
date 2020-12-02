package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.play.broadcaster.domain.usecase.UpdateChannelUseCase
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.ui.model.PlayChannelStatus
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_RFC3339
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class BroadcastScheduleDataStoreImpl @Inject constructor(
        private val dispatcher: CoroutineDispatcherProvider,
        private val updateChannelUseCase: UpdateChannelUseCase
): BroadcastScheduleDataStore {

    private val _selectedDateLiveData = MutableLiveData<BroadcastScheduleUiModel>()

    override fun getObservableSelectedDate(): LiveData<BroadcastScheduleUiModel> {
        return _selectedDateLiveData
    }

    override fun getSelectedDate(): BroadcastScheduleUiModel? {
        return _selectedDateLiveData.value
    }

    override fun setBroadcastSchedule(scheduleDate: BroadcastScheduleUiModel) {
        _selectedDateLiveData.value = scheduleDate
    }

    override suspend fun setBroadcastSchedule(channelId: String): NetworkResult<Unit> {
        return try {
            updateSchedule(channelId)
            getSelectedDate()?.let {
                setBroadcastSchedule(it)
            }
            NetworkResult.Success(Unit)
        } catch (e: Throwable) {
            NetworkResult.Fail(e)
        }
    }

    private suspend fun updateSchedule(channelId: String) = withContext(dispatcher.io) {
        val selectedDate = when (val currentScheduleDate = getSelectedDate()) {
            is BroadcastScheduleUiModel.Scheduled -> currentScheduleDate.time
            else -> throw IllegalStateException("Date must not be null")
        }

        updateChannelUseCase.apply {
            setQueryParams(
                    UpdateChannelUseCase.createUpdateBroadcastScheduleRequest(
                            channelId = channelId,
                            status = PlayChannelStatus.ScheduledLive,
                            date = selectedDate.toFormattedString(DATE_FORMAT_RFC3339)
                    )
            )
        }
        return@withContext updateChannelUseCase.executeOnBackground()
    }
}