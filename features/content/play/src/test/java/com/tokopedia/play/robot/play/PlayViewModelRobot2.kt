package com.tokopedia.play.robot.play

import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.usecase.TrackVisitChannelBroadcasterUseCase
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.PlayMapperBuilder
import com.tokopedia.play.util.CastPlayerHelper
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.chat.ChatManager
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play.util.logger.PlayLog
import com.tokopedia.play.util.share.PlayShareExperience
import com.tokopedia.play.util.timer.TimerFactory
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.monitoring.PlayVideoLatencyPerformanceMonitoring
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.PlayViewerNewUiEvent
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runBlockingTest
import java.io.Closeable

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelRobot2(
    channelId: String,
    playVideoBuilder: PlayVideoWrapper.Builder,
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
    getSocketCredentialUseCase: GetSocketCredentialUseCase,
    getReportSummariesUseCase: GetReportSummariesUseCase,
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineTestDispatchers,
    remoteConfig: RemoteConfig,
    playPreference: PlayPreference,
    videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
    playChannelWebSocket: PlayWebSocket,
    repo: PlayViewerRepository,
    playAnalytic: PlayNewAnalytic,
    timerFactory: TimerFactory,
    castPlayerHelper: CastPlayerHelper,
    playShareExperience: PlayShareExperience,
    chatManagerFactory: ChatManager.Factory,
    chatStreamsFactory: ChatStreams.Factory,
    playLog: PlayLog,
    liveRoomMetricsCommon: PlayLiveRoomMetricsCommon
) : Closeable {

    val viewModel: PlayViewModel = PlayViewModel(
        channelId,
        playVideoBuilder,
        videoStateProcessorFactory,
        channelStateProcessorFactory,
        videoBufferGovernorFactory,
        getReportSummariesUseCase,
        PlayMapperBuilder().buildSocketMapper(),
        ClassBuilder().getPlayUiModelMapper(userSession),
        userSession,
        dispatchers,
        remoteConfig,
        playPreference,
        videoLatencyPerformanceMonitoring,
        playChannelWebSocket,
        repo,
        playAnalytic,
        timerFactory,
        castPlayerHelper,
        playShareExperience,
        playLog,
        chatManagerFactory,
        chatStreamsFactory,
        liveRoomMetricsCommon
    )

    fun createPage(channelData: PlayChannelData) {
        viewModel.createPage(channelData)
    }

    fun focusPage(channelData: PlayChannelData) {
        viewModel.focusPage(channelData)
    }

    suspend fun submitAction(action: PlayViewerNewAction) = act {
        viewModel.submitAction(action)
    }

    fun setLoggedIn(isUserLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isUserLoggedIn
    }

    fun setUserId(userId: String) {
        every { userSession.userId } returns userId
    }

    fun sendChat(message: String) {
        viewModel.sendChat(message)
    }

    fun recordState(fn: suspend PlayViewModelRobot2.() -> Unit): PlayViewerNewUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayViewerNewUiState
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

    fun recordEvent(fn: suspend PlayViewModelRobot2.() -> Unit): List<PlayViewerNewUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<PlayViewerNewUiEvent>()
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

    fun recordStateAndEvent(fn: suspend PlayViewModelRobot2.() -> Unit): Pair<PlayViewerNewUiState, List<PlayViewerNewUiEvent>> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayViewerNewUiState
        val uiEvents = mutableListOf<PlayViewerNewUiEvent>()
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

    fun recordChatState(fn: suspend PlayViewModelRobot2.() -> Unit): List<PlayChatUiModel> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var chats: List<PlayChatUiModel>
        scope.launch {
            viewModel.chats.collect {
                chats = it
            }
        }
        dispatchers.coroutineDispatcher.runBlockingTest { fn() }
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return chats
    }

    fun cancelRemainingTasks() {
        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    private suspend fun act(fn: () -> Unit) {
        fn()
        yield()
    }

    override fun close() {
        cancelRemainingTasks()
    }
}

fun createPlayViewModelRobot(
    channelId: String = "1",
    dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    playVideoBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true),
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory = mockk(relaxed = true),
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory = mockk(relaxed = true),
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    getReportSummariesUseCase: GetReportSummariesUseCase = mockk(relaxed = true),
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    remoteConfig: RemoteConfig = mockk(relaxed = true),
    playPreference: PlayPreference = mockk(relaxed = true),
    videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring = mockk(relaxed = true),
    playChannelWebSocket: PlayWebSocket = mockk(relaxed = true),
    repo: PlayViewerRepository = mockk(relaxed = true),
    playAnalytic: PlayNewAnalytic = mockk(relaxed = true),
    timerFactory: TimerFactory = mockk(relaxed = true),
    castPlayerHelper: CastPlayerHelper = mockk(relaxed = true),
    playShareExperience: PlayShareExperience = mockk(relaxed = true),
    chatManagerFactory: ChatManager.Factory = mockk(relaxed = true),
    chatStreamsFactory: ChatStreams.Factory = mockk(relaxed = true),
    playLog: PlayLog = mockk(relaxed = true),
    liveRoomMetricsCommon: PlayLiveRoomMetricsCommon = mockk(relaxed = true),
    fn: PlayViewModelRobot2.() -> Unit = {}
): PlayViewModelRobot2 {
    return PlayViewModelRobot2(
        channelId = channelId,
        playVideoBuilder = playVideoBuilder,
        videoStateProcessorFactory = videoStateProcessorFactory,
        channelStateProcessorFactory = channelStateProcessorFactory,
        videoBufferGovernorFactory = videoBufferGovernorFactory,
        getSocketCredentialUseCase = getSocketCredentialUseCase,
        getReportSummariesUseCase = getReportSummariesUseCase,
        trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
        userSession = userSession,
        dispatchers = dispatchers,
        remoteConfig = remoteConfig,
        playPreference = playPreference,
        videoLatencyPerformanceMonitoring = videoLatencyPerformanceMonitoring,
        playChannelWebSocket = playChannelWebSocket,
        repo = repo,
        playAnalytic = playAnalytic,
        timerFactory = timerFactory,
        castPlayerHelper = castPlayerHelper,
        playShareExperience = playShareExperience,
        chatManagerFactory = chatManagerFactory,
        chatStreamsFactory = chatStreamsFactory,
        playLog = playLog,
        liveRoomMetricsCommon = liveRoomMetricsCommon
    ).apply(fn)
}
