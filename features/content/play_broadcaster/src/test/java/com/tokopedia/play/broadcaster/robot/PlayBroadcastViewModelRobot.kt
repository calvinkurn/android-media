package com.tokopedia.play.broadcaster.robot

import androidx.lifecycle.viewModelScope
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.socket.PlayBroadcastWebSocket
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.usecase.*
import com.tokopedia.play.broadcaster.domain.usecase.interactive.GetInteractiveConfigUseCase
import com.tokopedia.play.broadcaster.domain.usecase.interactive.PostInteractiveCreateSessionUseCase
import com.tokopedia.play.broadcaster.pusher.mediator.PusherMediator
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastUiState
import com.tokopedia.play.broadcaster.util.TestHtmlTextTransformer
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.domain.usecase.interactive.GetCurrentInteractiveUseCase
import com.tokopedia.play_common.domain.usecase.interactive.GetInteractiveLeaderboardUseCase
import com.tokopedia.play_common.model.mapper.PlayChannelInteractiveMapper
import com.tokopedia.play_common.model.mapper.PlayInteractiveLeaderboardMapper
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 06/10/21
 */
internal class PlayBroadcastViewModelRobot(
    private val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    livePusherMediator: PusherMediator = mockk(relaxed = true),
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
    channelRepo: PlayBroadcastChannelRepository = mockk(relaxed = true),
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
        channelRepo
    )

    fun recordState(fn: PlayBroadcastViewModelRobot.() -> Unit): PlayBroadcastUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayBroadcastUiState
        scope.launch {
            viewModel.uiState.collect {
                uiState = it
            }
        }
        fn()
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiState
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    fun getConfig() = viewModel.getConfiguration()
}