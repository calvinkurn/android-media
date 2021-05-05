package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class BroadcastScheduleViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    val minScheduleDate: Date
        get() = hydraConfigStore.getMinScheduleDate()

    val maxScheduleDate: Date
        get() = hydraConfigStore.getMaxScheduleDate()

    val defaultScheduleDate: Date
        get() = getCurrentSelectedDate() ?: hydraConfigStore.getDefaultScheduleDate()

    private val channelId: String
        get() = hydraConfigStore.getChannelId()

    val schedule: BroadcastScheduleUiModel
        get() = setupDataStore.getSchedule() ?: BroadcastScheduleUiModel.NoSchedule

    val observableSetBroadcastSchedule: LiveData<NetworkResult<Unit>>
        get() = _observableSetBroadcastSchedule
    private val _observableSetBroadcastSchedule = MutableLiveData<NetworkResult<Unit>>()

    val observableDeleteBroadcastSchedule: LiveData<NetworkResult<Unit>>
        get() = _observableDeleteBroadcastSchedule
    private val _observableDeleteBroadcastSchedule = MutableLiveData<NetworkResult<Unit>>()

    private fun getCurrentSelectedDate() = when (val selectedDate = setupDataStore.getSchedule()) {
        is BroadcastScheduleUiModel.Scheduled -> selectedDate.time
        else -> null
    }

    fun setBroadcastSchedule(selectedCalendar: Calendar) {
        viewModelScope.launch {
            _observableSetBroadcastSchedule.value = NetworkResult.Loading
            val value = setupDataStore.updateBroadcastSchedule(channelId, selectedCalendar.time)
            _observableSetBroadcastSchedule.value = value
        }
    }

    fun deleteBroadcastSchedule() {
        viewModelScope.launch {
            _observableDeleteBroadcastSchedule.value = NetworkResult.Loading
            val value = setupDataStore.deleteBroadcastSchedule(channelId)
            _observableDeleteBroadcastSchedule.value = value
        }
    }
}