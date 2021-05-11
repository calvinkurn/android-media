package com.tokopedia.play.broadcaster.robot

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.view.viewmodel.PlayTitleAndTagsSetupViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk

/**
 * Created by jegul on 11/05/21
 */
class PlayTitleAndTagsViewModelRobot(
        private val hydraConfigStore: HydraConfigStore,
        private val dispatcher: CoroutineDispatchers,
        private val setupDataStore: PlayBroadcastSetupDataStore,
        private val getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase
) : Robot {

    val viewModel: PlayTitleAndTagsSetupViewModel = PlayTitleAndTagsSetupViewModel(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore,
            getRecommendedChannelTagsUseCase = getRecommendedChannelTagsUseCase,
    )

    fun toggleTag(tag: String) {
        viewModel.toggleTag(tag)
    }
}

fun givenPlayTitleAndTagsViewModel(
        hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        setupDataStore: PlayBroadcastSetupDataStore = mockk(relaxed = true),
        getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true),
        fn: PlayTitleAndTagsViewModelRobot.() -> Unit = {},
) : PlayTitleAndTagsViewModelRobot {
    return PlayTitleAndTagsViewModelRobot(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore,
            getRecommendedChannelTagsUseCase = getRecommendedChannelTagsUseCase
    ).apply(fn)
}