package com.tokopedia.play.broadcaster.robot

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.usecase.GetRecommendedChannelTagsUseCase
import com.tokopedia.play.broadcaster.util.TestDoubleModelBuilder
import com.tokopedia.play.broadcaster.util.getOrAwaitValue
import com.tokopedia.play.broadcaster.view.viewmodel.PlayTitleAndTagsSetupViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk

/**
 * Created by jegul on 11/05/21
 */
class PlayTitleAndTagsViewModelRobot(
        hydraConfigStore: HydraConfigStore,
        dispatcher: CoroutineDispatchers,
        setupDataStore: PlayBroadcastSetupDataStore,
        getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase
) : Robot {

    private val viewModel: PlayTitleAndTagsSetupViewModel = PlayTitleAndTagsSetupViewModel(
            hydraConfigStore = hydraConfigStore,
            dispatcher = dispatcher,
            setupDataStore = setupDataStore,
            getRecommendedChannelTagsUseCase = getRecommendedChannelTagsUseCase,
    )

    fun getAddedTags() = viewModel.addedTags

    fun toggleTag(tag: String) {
        viewModel.toggleTag(tag)
    }

    fun finishSetup(title: String) {
        viewModel.finishSetup(title)
    }

    fun isTagValid(tag: String): Boolean {
        return viewModel.isTagValid(tag)
    }

    fun isTitleValid(title: String): Boolean {
        return viewModel.isTitleValid(title)
    }

    fun getSavedTitle() = viewModel.observableTitle.getOrAwaitValue()
}

fun givenPlayTitleAndTagsViewModel(
        hydraConfigStore: HydraConfigStore = TestDoubleModelBuilder().buildHydraConfigStore(),
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        setupDataStore: PlayBroadcastSetupDataStore = TestDoubleModelBuilder().buildSetupDataStore(),
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