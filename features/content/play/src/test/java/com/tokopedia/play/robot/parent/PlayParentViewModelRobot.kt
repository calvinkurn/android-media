package com.tokopedia.play.robot.parent

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.uimodel.mapper.PlayChannelDetailsWithRecomMapper
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play_common.model.result.PageResult
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions

/**
 * Created by jegul on 10/02/21
 */
class PlayParentViewModelRobot(
        private val savedStateHandle: SavedStateHandle,
        private val channelStateStorage: PlayChannelStateStorage,
        private val getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase,
        private val playChannelMapper: PlayChannelDetailsWithRecomMapper,
        private val dispatchers: TestCoroutineDispatchersProvider,
        private val userSession: UserSessionInterface,
) {

    val viewModel: PlayParentViewModel

    init {
        every { savedStateHandle.get<String>(any()) } returns "123"

        viewModel = PlayParentViewModel(
                handle = savedStateHandle,
                playChannelStateStorage = channelStateStorage,
                getChannelDetailsWithRecomUseCase = getChannelDetailsWithRecomUseCase,
                playChannelMapper = playChannelMapper,
                dispatchers = dispatchers,
                userSession = userSession
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
        playChannelMapper: PlayChannelDetailsWithRecomMapper = PlayChannelDetailsWithRecomMapper(),
        dispatchers: TestCoroutineDispatchersProvider = TestCoroutineDispatchersProvider,
        userSession: UserSessionInterface = mockk(relaxed = true),
): PlayParentViewModelRobot {
    return PlayParentViewModelRobot(
            savedStateHandle = savedStateHandle,
            channelStateStorage = channelStateStorage,
            getChannelDetailsWithRecomUseCase = getChannelDetailsWithRecomUseCase,
            playChannelMapper = playChannelMapper,
            dispatchers = dispatchers,
            userSession = userSession
    )
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