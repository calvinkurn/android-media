package com.tokopedia.play.broadcaster.data.datastore

import androidx.lifecycle.LiveData
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import java.util.*


/**
 * Created by mzennis on 01/12/20.
 */
interface BroadcastScheduleDataStore {

    fun getObservableSchedule(): LiveData<BroadcastScheduleUiModel>

    fun getSchedule(): BroadcastScheduleUiModel?

    fun setBroadcastSchedule(scheduleDate: BroadcastScheduleUiModel)

    suspend fun updateBroadcastSchedule(channelId: String, scheduledTime: Date): NetworkResult<Unit>

    suspend fun deleteBroadcastSchedule(channelId: String): NetworkResult<Unit>
}