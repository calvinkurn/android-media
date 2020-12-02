package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.ui.model.BroadcastScheduleUiModel
import com.tokopedia.play.broadcaster.util.extension.DATE_FORMAT_BROADCAST_SCHEDULE
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.result.map
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


/**
 * Created by mzennis on 01/12/20.
 */
class SetupBroadcastScheduleViewModel @Inject constructor(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    val minScheduleDate: Date
        get() = hydraConfigStore.getMinScheduleDate()

    val maxScheduleDate: Date
        get() = hydraConfigStore.getMaxScheduleDate()

    val defaultScheduleDate: Date
        get() = getCurrentSelectedDate()?:hydraConfigStore.getDefaultScheduleDate()

    private val channelId: String
        get() = hydraConfigStore.getChannelId()

    val observableSetBroadcastSchedule: LiveData<NetworkResult<Unit>>
        get() = _observableSetBroadcastSchedule
    private val _observableSetBroadcastSchedule = MutableLiveData<NetworkResult<Unit>>()

    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatcher.main + job)

    private fun getCurrentSelectedDate() = when (val selectedDate = setupDataStore.getSelectedDate()) {
        is BroadcastScheduleUiModel.Scheduled -> selectedDate.time
        else -> null
    }

    fun setBroadcastSchedule(selectedCalendar: Calendar) {
        setupDataStore.setBroadcastSchedule(
                BroadcastScheduleUiModel.Scheduled(
                        time = selectedCalendar.time,
                        formattedTime = selectedCalendar.time.toFormattedString(DATE_FORMAT_BROADCAST_SCHEDULE, Locale("id", "ID"))
                )
        )

        scope.launch {
            _observableSetBroadcastSchedule.value = NetworkResult.Loading
            val value = setupDataStore.setBroadcastSchedule(channelId).map { Unit }
            _observableSetBroadcastSchedule.value = value
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}