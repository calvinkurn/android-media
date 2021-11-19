package com.tokopedia.play.robot.upcoming

import androidx.lifecycle.viewModelScope
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.domain.GetChannelStatusUseCase
import com.tokopedia.play.domain.GetSocketCredentialUseCase
import com.tokopedia.play.domain.PlayChannelReminderUseCase
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.robot.play.PlayViewModelRobot2
import com.tokopedia.play.view.uimodel.action.PlayUpcomingAction
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.PlayUpcomingUiEvent
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.state.PlayUpcomingUiState
import com.tokopedia.play.view.viewmodel.PlayUpcomingViewModel
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on November 18, 2021
 */
class PlayUpcomingViewModelRobot(
    getChannelStatusUseCase: GetChannelStatusUseCase = mockk(relaxed = true),
    playChannelReminderUseCase: PlayChannelReminderUseCase = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    userSession: UserSessionInterface = mockk(relaxed = true),
    playUiModelMapper: PlayUiModelMapper = mockk(relaxed = true),
    playAnalytic: PlayNewAnalytic = mockk(relaxed = true),
    playChannelSSE: PlayChannelSSE = mockk(relaxed = true),
    repo: PlayViewerRepository = mockk(relaxed = true),
): Closeable {

    val viewModel = PlayUpcomingViewModel(
        getChannelStatusUseCase= getChannelStatusUseCase,
        playChannelReminderUseCase = playChannelReminderUseCase,
        getSocketCredentialUseCase = getSocketCredentialUseCase,
        dispatchers = dispatchers,
        userSession = userSession,
        playUiModelMapper = playUiModelMapper,
        playAnalytic = playAnalytic,
        playChannelSSE = playChannelSSE,
        repo = repo,
    )

    suspend fun submitAction(action: PlayUpcomingAction) = act {
        viewModel.submitAction(action)
    }

    fun recordState(fn: suspend PlayUpcomingViewModelRobot.() -> Unit): PlayUpcomingUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayUpcomingUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordEvent(fn: suspend PlayUpcomingViewModelRobot.() -> Unit): List<PlayUpcomingUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<PlayUpcomingUiEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvents.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEvents
    }

    fun recordStateAndEvent(fn: suspend PlayUpcomingViewModelRobot.() -> Unit): Pair<PlayUpcomingUiState, List<PlayUpcomingUiEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayUpcomingUiState
        val uiEvents = mutableListOf<PlayUpcomingUiEvent>()
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvents.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState to uiEvents
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}

fun createPlayUpcomingViewModelRobot(
    dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    getChannelStatusUseCase: GetChannelStatusUseCase  = mockk(relaxed = true),
    playChannelReminderUseCase: PlayChannelReminderUseCase  = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase  = mockk(relaxed = true),
    userSession: UserSessionInterface  = mockk(relaxed = true),
    playUiModelMapper: PlayUiModelMapper  = mockk(relaxed = true),
    playAnalytic: PlayNewAnalytic  = mockk(relaxed = true),
    playChannelSSE: PlayChannelSSE  = mockk(relaxed = true),
    repo: PlayViewerRepository  = mockk(relaxed = true),
    fn: PlayUpcomingViewModelRobot.() -> Unit = {}
): PlayUpcomingViewModelRobot {
    return PlayUpcomingViewModelRobot(
        getChannelStatusUseCase = getChannelStatusUseCase,
        playChannelReminderUseCase = playChannelReminderUseCase,
        getSocketCredentialUseCase = getSocketCredentialUseCase,
        dispatchers = dispatchers,
        userSession = userSession,
        playUiModelMapper = playUiModelMapper,
        playAnalytic = playAnalytic,
        playChannelSSE = playChannelSSE,
        repo = repo,
    ).apply(fn)
}