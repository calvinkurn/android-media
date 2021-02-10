package com.tokopedia.play.robot

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.data.detail.recom.ChannelDetailsWithRecomResponse
import com.tokopedia.play.domain.GetChannelDetailsWithRecomUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
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

    private val viewModel: PlayParentViewModel

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

    inner class Result {

        val channelIdResult: ChannelIdResult
            get() = ChannelIdResult()
    }

    inner class ChannelIdResult {

        private val result = viewModel.observableChannelIdsResult.getOrAwaitValue()

        fun isNotEmpty(): ChannelIdResult {
            Assertions
                    .assertThat(result.currentValue)
                    .isNotEmpty

            return this
        }

        fun isEmpty(): ChannelIdResult {
            Assertions
                    .assertThat(result.currentValue)
                    .isEmpty()

            return this
        }

        fun isSuccess(): ChannelIdResult {
            Assertions
                    .assertThat(result.state)
                    .isInstanceOf(PageResultState.Success::class.java)

            return this
        }

        fun isFailure(): ChannelIdResult {
            Assertions
                    .assertThat(result.state)
                    .isInstanceOf(PageResultState.Fail::class.java)

            return this
        }
    }
}

fun givenParentViewModelRobot(
        savedStateHandle: SavedStateHandle = mockk(relaxed = true),
        channelStateStorage: PlayChannelStateStorage = PlayChannelStateStorage(),
        getChannelDetailsWithRecomUseCase: GetChannelDetailsWithRecomUseCase = mockk(relaxed = true),
        playChannelMapper: PlayChannelDetailsWithRecomMapper = PlayChannelDetailsWithRecomMapper(),
        dispatchers: TestCoroutineDispatchersProvider = TestCoroutineDispatchersProvider,
        userSession: UserSessionInterface = mockk(relaxed = true),
        fn: PlayParentViewModelRobot.() -> Unit
): PlayParentViewModelRobot {
    return PlayParentViewModelRobot(
            savedStateHandle = savedStateHandle,
            channelStateStorage = channelStateStorage,
            getChannelDetailsWithRecomUseCase = getChannelDetailsWithRecomUseCase,
            playChannelMapper = playChannelMapper,
            dispatchers = dispatchers,
            userSession = userSession
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
        fn: PlayParentViewModelRobot.Result.() -> Unit
): PlayParentViewModelRobot {
    Result().apply(fn)
    return this
}