package com.tokopedia.play.broadcaster.robot

import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.util.TestDoubleModelBuilder
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.BroadcastScheduleViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import java.util.*

/**
 * Created by jegul on 21/05/21
 */
class BroadcastScheduleViewModelRobot(
        hydraConfigStore: HydraConfigStore,
        dispatcher: CoroutineTestDispatchers,
        setupDataStore: PlayBroadcastSetupDataStore,
) : Robot {

    private val viewModel: BroadcastScheduleViewModel = BroadcastScheduleViewModel(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore
    )

    fun getUpdateScheduleResult() = viewModel.observableSetBroadcastSchedule.getOrAwaitValue()

    fun getDeleteScheduleResult() = viewModel.observableDeleteBroadcastSchedule.getOrAwaitValue()

    fun getDefaultScheduleDate() = viewModel.defaultScheduleDate

    fun getMinimumScheduleDate() = viewModel.minScheduleDate

    fun getMaximumScheduleDate() = viewModel.maxScheduleDate

    fun getSchedule() = viewModel.schedule

    fun setSchedule(calendar: Calendar) {
        viewModel.setBroadcastSchedule(calendar)
    }

    fun deleteSchedule() {
        viewModel.deleteBroadcastSchedule()
    }
}

fun givenBroadcastScheduleViewModel(
        hydraConfigStore: HydraConfigStore = TestDoubleModelBuilder().buildHydraConfigStore(),
        dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
        setupDataStore: PlayBroadcastSetupDataStore = TestDoubleModelBuilder().buildSetupDataStore(),
        fn: BroadcastScheduleViewModelRobot.() -> Unit = {},
) : BroadcastScheduleViewModelRobot {
    return BroadcastScheduleViewModelRobot(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore
    ).apply(fn)
}