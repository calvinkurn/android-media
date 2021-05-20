package com.tokopedia.play.robot.parent

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

/**
 * Created by jegul on 10/02/21
 */
class PlayParentViewModelRobot(
        private val savedStateHandle: SavedStateHandle,
        private val channelStateStorage: PlayChannelStateStorage,
        private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
        private val playChannelMapper: PlayChannelDetailsWithRecomMapper,
        private val dispatchers: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        pageMonitoring: PlayPltPerformanceCallback,
) {

    val viewModel: PlayParentViewModel

    init {
        every { savedStateHandle.get<Any>(any()) } returns "123"
        every { savedStateHandle.get<Any>("start_vod_millis") } returns 123L

        viewModel = PlayParentViewModel(
                handle = savedStateHandle,
                playChannelStateStorage = channelStateStorage,
                getChannelDetailsWithRecomUseCase = getChannelDetailsWithRecomUseCase,
                playChannelMapper = playChannelMapper,
                dispatchers = dispatchers,
                userSession = userSession,
                pageMonitoring = pageMonitoring,
        )
    }

    fun mockChannelDetailsResponse(response: ChannelDetailsWithRecomResponse) {
        coEvery { getChannelDetailsWithRecomUseCase.executeOnBackground() } returns response
    }

    fun setChannelData(channelId: String, channelData: PlayChannelData) = viewModel.setLatestChannelStorageData(channelId, channelData)
}

fun givenParentViewModelRobot(
        savedStateHandle: SavedStateHandle = mockk(relaxed = true),
        channelStateStorage: PlayChannelStateStorage = PlayChannelStateStorage(),
        getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true),
        playChannelMapper: PlayChannelDetailsWithRecomMapper = ClassBuilder().getPlayChannelDetailsRecomMapper(),
        dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider,
        userSession: UserSessionInterface = mockk(relaxed = true),
        pageMonitoring: PlayPltPerformanceCallback = mockk(relaxed = true),
        fn: PlayParentViewModelRobot.() -> Unit = {}
): PlayParentViewModelRobot {
    return PlayParentViewModelRobot(
            savedStateHandle = savedStateHandle,
            channelStateStorage = channelStateStorage,
            getChannelDetailsWithRecomUseCase = getChannelDetailsWithRecomUseCase,
            playChannelMapper = playChannelMapper,
            dispatchers = dispatchers,
            userSession = userSession,
            pageMonitoring = pageMonitoring
    ).apply(fn)
}

infix fun PlayParentViewModelRobot.andWhen(
        fn: PlayParentViewModelRobot.() -> Unit
): PlayParentViewModelRobot {
    return apply(fn)
}

infix fun PlayParentViewModelRobot.andThen(
        fn: PlayParentViewModelRobot.() -> Unit
): PlayParentViewModelRobot {
    return apply(fn)
}

infix fun PlayParentViewModelRobot.thenVerify(
        fn: PlayParentViewModelRobotResult.() -> Unit
): PlayParentViewModelRobot {
    PlayParentViewModelRobotResult(viewModel).apply { fn() }
    return this
}