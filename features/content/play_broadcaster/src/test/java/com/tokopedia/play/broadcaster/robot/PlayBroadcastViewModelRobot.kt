package com.tokopedia.play.broadcaster.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastWebSocket
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveConfigUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.PostInteractiveCreateSessionUseCase
import com.tokopedia.play.broadcaster.logger.PlayLoggerTest
import com.tokopedia.play.broadcaster.pusher.PlayLivePusherMediator
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastUiState
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created by jegul on 06/10/21
 */
internal class PlayBroadcastViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    livePusherMediator: PlayLivePusherMediator = mockk(relaxed = true),
    mDataStore: PlayBroadcastDataStore = mockk(relaxed = true),
    hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
    sharedPref: HydraSharedPreferences = mockk(relaxed = true),
    getChannelUseCase: GetChannelUseCase = mockk(relaxed = true),
    createChannelUseCase: CreateChannelUseCase = mockk(relaxed = true),
    updateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true),
    getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    getInteractiveConfigUseCase: GetInteractiveConfigUseCase = mockk(relaxed = true),
    getCurrentInteractiveUseCase: GetCurrentInteractiveUseCase = mockk(relaxed = true),
    getInteractiveLeaderboardUseCase: GetInteractiveLeaderboardUseCase = mockk(relaxed = true),
    createInteractiveSessionUseCase: PostInteractiveCreateSessionUseCase = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    playBroadcastWebSocket: PlayBroadcastWebSocket = mockk(relaxed = true),
    playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(TestHtmlTextTransformer()),
    channelInteractiveMapper: PlayChannelInteractiveMapper = mockk(relaxed = true),
    interactiveLeaderboardMapper: PlayInteractiveLeaderboardMapper = mockk(relaxed = true),
    channelRepo: PlayBroadcastRepository = mockk(relaxed = true),
    logger: PlayLogger = mockk(relaxed = true),
) {

    private val viewModel = PlayBroadcastViewModel(
        livePusherMediator,
        mDataStore,
        hydraConfigStore,
        sharedPref,
        getChannelUseCase,
        createChannelUseCase,
        updateChannelUseCase,
        getAddedChannelTagsUseCase,
        getSocketCredentialUseCase,
        getInteractiveConfigUseCase,
        getCurrentInteractiveUseCase,
        getInteractiveLeaderboardUseCase,
        createInteractiveSessionUseCase,
        dispatchers,
        userSession,
        playBroadcastWebSocket,
        playBroadcastMapper,
        channelInteractiveMapper,
        interactiveLeaderboardMapper,
        channelRepo,
        logger,
    )

    fun recordState(fn: suspend PlayBroadcastViewModelRobot.() -> Unit): PlayBroadcastUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroadcastUiState
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

    fun recordStateAsList(fn: suspend PlayBroadcastViewModelRobot.() -> Unit): List<PlayBroadcastUiState> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiStateList = mutableListOf<PlayBroadcastUiState>()
        scope.launch {
            viewModel.uiState.collect {
                uiStateList.add(it)
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiStateList
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    fun getConfig() = viewModel.getConfiguration()

    fun startLive() = viewModel.startLiveStream()

    suspend fun setPinned(message: String) = act {
        viewModel.submitAction(PlayBroadcastAction.SetPinnedMessage(message))
    }

    suspend fun editPinned() = act {
        viewModel.submitAction(PlayBroadcastAction.EditPinnedMessage)
    }

    suspend fun cancelEditPinned() = act {
        viewModel.submitAction(PlayBroadcastAction.CancelEditPinnedMessage)
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    fun getViewModel() = viewModel

    fun <T> getViewModelPrivateField(name: String): T {
        val field = viewModel.javaClass.getDeclaredField(name)
        field.isAccessible = true
        return field.get(viewModel) as T
    }

    override fun close() {
        cancelRemainingTasks()
    }
}