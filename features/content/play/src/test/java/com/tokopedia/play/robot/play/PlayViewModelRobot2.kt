package com.tokopedia.play.robot.play

import com.google.android.gms.cast.framework.CastContext
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.robot.Robot
import com.tokopedia.play.util.CastPlayerHelper
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.timer.TimerFactory
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.monitoring.PlayVideoLatencyPerformanceMonitoring
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.PlayViewerNewUiEvent
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.sse.PlayChannelSSE
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelRobot2(
    private val playVideoBuilder: PlayVideoWrapper.Builder,
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
    getChannelStatusUseCase: GetChannelStatusUseCase,
    getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val getReportSummariesUseCase: GetReportSummariesUseCase,
    getProductTagItemsUseCase: GetProductTagItemsUseCase,
    trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase,
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    playChannelReminderUseCase: PlayChannelReminderUseCase,
    playSocketToModelMapper: PlaySocketToModelMapper,
    playUiModelMapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineTestDispatchers,
    remoteConfig: RemoteConfig,
    playPreference: PlayPreference,
    videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
    playChannelWebSocket: PlayChannelWebSocket,
    playChannelSSE: PlayChannelSSE,
    private val repo: PlayViewerRepository,
    playAnalytic: PlayNewAnalytic,
    timerFactory: TimerFactory,
    castPlayerHelper: CastPlayerHelper
) : Robot {

    val viewModel: PlayViewModel

    init {
        viewModel = PlayViewModel(
            playVideoBuilder,
            videoStateProcessorFactory,
            channelStateProcessorFactory,
            videoBufferGovernorFactory,
            getChannelStatusUseCase,
            getSocketCredentialUseCase,
            getReportSummariesUseCase,
            getProductTagItemsUseCase,
            trackProductTagBroadcasterUseCase,
            trackVisitChannelBroadcasterUseCase,
            playChannelReminderUseCase,
            playSocketToModelMapper,
            playUiModelMapper,
            userSession,
            dispatchers,
            remoteConfig,
            playPreference,
            videoLatencyPerformanceMonitoring,
            playChannelWebSocket,
            playChannelSSE,
            repo,
            playAnalytic,
            timerFactory,
            castPlayerHelper
        )
    }

    fun createPage(channelData: PlayChannelData) {
        viewModel.createPage(channelData)
    }

    fun focusPage(channelData: PlayChannelData) {
        viewModel.focusPage(channelData)
    }

    fun submitAction(action: PlayViewerNewAction) {
        viewModel.submitAction(action)
    }

    fun setLoggedIn(isUserLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isUserLoggedIn
    }

    fun recordState(fn: PlayViewModelRobot2.() -> Unit): PlayViewerNewUiState {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        lateinit var uiState: PlayViewerNewUiState
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

    fun recordEvent(fn: PlayViewModelRobot2.() -> Unit): List<PlayViewerNewUiEvent> {
        val scope = CoroutineScope(dispatchers.coroutineDispatcher)
        val uiEvents = mutableListOf<PlayViewerNewUiEvent>()
        scope.launch {
            viewModel.uiEvent.collect {
                uiEvents.add(it)
            }
        }
        fn()
        dispatchers.coroutineDispatcher.advanceUntilIdle()
        scope.cancel()
        return uiEvents
    }

    fun recordStateAndEvent(fn: PlayViewModelRobot2.() -> Unit): Pair<PlayViewerNewUiState, List<PlayViewerNewUiEvent>> {
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
        fn()
        dispatchers.coroutineDispatcher.runCurrent()
        scope.cancel()
        return uiState to uiEvents
    }
}

fun createPlayViewModelRobot(
    dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers,
    playVideoBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true),
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory = mockk(relaxed = true),
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory = mockk(relaxed = true),
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory = mockk(relaxed = true),
    getChannelStatusUseCase: GetChannelStatusUseCase = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    getReportSummariesUseCase: GetReportSummariesUseCase = mockk(relaxed = true),
    getProductTagItemsUseCase: GetProductTagItemsUseCase = mockk(relaxed = true),
    trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase = mockk(relaxed = true),
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
    playChannelReminderUseCase: PlayChannelReminderUseCase = mockk(relaxed = true),
    playSocketToModelMapper: PlaySocketToModelMapper = mockk(relaxed = true),
    playUiModelMapper: PlayUiModelMapper = ClassBuilder().getPlayUiModelMapper(),
    userSession: UserSessionInterface = mockk(relaxed = true),
    remoteConfig: RemoteConfig = mockk(relaxed = true),
    playPreference: PlayPreference = mockk(relaxed = true),
    videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring = mockk(relaxed = true),
    playChannelWebSocket: PlayChannelWebSocket = mockk(relaxed = true),
    playChannelSSE: PlayChannelSSE = mockk(relaxed = true),
    repo: PlayViewerRepository = mockk(relaxed = true),
    playAnalytic: PlayNewAnalytic = mockk(relaxed = true),
    timerFactory: TimerFactory = mockk(relaxed = true),
    castPlayerHelper: CastPlayerHelper = mockk(relaxed = true),
    fn: PlayViewModelRobot2.() -> Unit = {}
): PlayViewModelRobot2 {
    return PlayViewModelRobot2(
        playVideoBuilder = playVideoBuilder,
        videoStateProcessorFactory = videoStateProcessorFactory,
        channelStateProcessorFactory = channelStateProcessorFactory,
        videoBufferGovernorFactory = videoBufferGovernorFactory,
        getChannelStatusUseCase = getChannelStatusUseCase,
        getSocketCredentialUseCase = getSocketCredentialUseCase,
        getReportSummariesUseCase = getReportSummariesUseCase,
        getProductTagItemsUseCase = getProductTagItemsUseCase,
        trackProductTagBroadcasterUseCase = trackProductTagBroadcasterUseCase,
        trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
        playChannelReminderUseCase = playChannelReminderUseCase,
        playSocketToModelMapper = playSocketToModelMapper,
        playUiModelMapper = playUiModelMapper,
        userSession = userSession,
        dispatchers = dispatchers,
        remoteConfig = remoteConfig,
        playPreference = playPreference,
        videoLatencyPerformanceMonitoring = videoLatencyPerformanceMonitoring,
        playChannelWebSocket = playChannelWebSocket,
        playChannelSSE = playChannelSSE,
        repo = repo,
        playAnalytic = playAnalytic,
        timerFactory = timerFactory,
        castPlayerHelper = castPlayerHelper
    ).apply(fn)
}