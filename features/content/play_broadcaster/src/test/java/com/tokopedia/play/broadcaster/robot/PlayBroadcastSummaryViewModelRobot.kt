package com.tokopedia.play.broadcaster.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastSummaryAction
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastSummaryEvent
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastSummaryUiState
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastSummaryViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created By : Jonathan Darwin on March 11, 2022
 */
class PlayBroadcastSummaryViewModelRobot(
    channelConfigStore: ChannelConfigStore = mockk(relaxed = true),
    private val dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
    getLiveStatisticsUseCase: GetLiveStatisticsUseCase = mockk(relaxed = true),
    updateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer()),
    getRecommendedChannelTagsUseCase: GetRecommendedChannelTagsUseCase = mockk(relaxed = true),
    setChannelTagsUseCase: SetChannelTagsUseCase = mockk(relaxed = true),
    getChannelUseCase: GetChannelUseCase = mockk(relaxed = true),
) : Closeable {

    private val viewModel = PlayBroadcastSummaryViewModel(
        channelConfigStore = channelConfigStore,
        dispatcher = dispatcher,
        getLiveStatisticsUseCase = getLiveStatisticsUseCase,
        updateChannelUseCase = updateChannelUseCase,
        userSession = userSession,
        playBroadcastMapper = playBroadcastMapper,
        getRecommendedChannelTagsUseCase = getRecommendedChannelTagsUseCase,
        setChannelTagsUseCase = setChannelTagsUseCase,
        getChannelUseCase = getChannelUseCase,
    )

    fun recordState(fn: suspend PlayBroadcastSummaryViewModelRobot.() -> Unit): PlayBroadcastSummaryUiState {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: PlayBroadcastSummaryUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun recordStateAsList(fn: suspend PlayBroadcastSummaryViewModelRobot.() -> Unit): List<PlayBroadcastSummaryUiState> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiStateList = mutableListOf<PlayBroadcastSummaryUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    fun recordEvent(fn: suspend PlayBroadcastSummaryViewModelRobot.() -> Unit): List<PlayBroadcastSummaryEvent> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        val uiEventList = mutableListOf<PlayBroadcastSummaryEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEventList.add(it)
            }
        }
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEventList
    }

    fun recordStateAndEvent(fn: suspend PlayBroadcastSummaryViewModelRobot.() -> Unit): Pair<PlayBroadcastSummaryUiState, List<PlayBroadcastSummaryEvent>> {
        val scope = CoroutineScope(dispatcher.coroutineDispatcher)
        lateinit var uiState: PlayBroadcastSummaryUiState
        val uiEvents = mutableListOf<PlayBroadcastSummaryEvent>()
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
        dispatcher.coroutineDispatcher.runBlockingTest { fn() }
        dispatcher.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState to uiEvents
    }

    suspend fun submitAction(action: PlayBroadcastSummaryAction) = act {
        viewModel.submitAction(action)
    }

    fun getViewModel() = viewModel

    fun <T> getViewModelPrivateField(name: String): T {
        val field = viewModel.javaClass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(viewModel) as T
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