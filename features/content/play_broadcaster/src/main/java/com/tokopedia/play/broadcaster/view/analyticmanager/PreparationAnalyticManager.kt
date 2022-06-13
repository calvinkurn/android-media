package com.tokopedia.play.broadcaster.view.analyticmanager

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.analytic.setup.schedule.PlayBroScheduleAnalytic
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPreparationFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 29/03/22
 */
class PreparationAnalyticManager @Inject constructor(
    private val analytic: PlayBroScheduleAnalytic,
    private val dispatchers: CoroutineDispatchers,
) {

    fun observe(
        scope: CoroutineScope,
        event: EventBus<out Any>,
    ) {
        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
                when (it) {
                    PlayBroadcastPreparationFragment.Event.CloseSetupSchedule -> {
                        analytic.clickCloseSetupSchedule()
                    }
                    PlayBroadcastPreparationFragment.Event.ClickSetSchedule -> {
                        analytic.clickSetSchedule()
                    }
                    is PlayBroadcastPreparationFragment.Event.SaveSchedule -> {
                        analytic.clickSaveSchedule()
                    }
                    PlayBroadcastPreparationFragment.Event.DeleteSchedule -> {
                        analytic.clickDeleteSchedule()
                    }
                }
            }
        }
    }
}