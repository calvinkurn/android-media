package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play_common.model.result.NetworkResult


/**
 * Created by mzennis on 01/12/20.
 */
interface BroadcastScheduleDataStore {

    fun getObservableSelectedDate(): LiveData<BroadcastScheduleUiModel>

    fun getSelectedDate(): BroadcastScheduleUiModel?

    fun setBroadcastSchedule(scheduleDate: BroadcastScheduleUiModel)

    suspend fun setBroadcastSchedule(channelId: String): NetworkResult<Unit>
}