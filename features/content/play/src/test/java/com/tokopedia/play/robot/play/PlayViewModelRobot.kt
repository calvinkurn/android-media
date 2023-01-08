package com.tokopedia.play.robot.play

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.analytic.PlayNewAnalytic
import com.tokopedia.play.data.ReportSummaries
import com.tokopedia.play.domain.*
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.PlayProductTagsModelBuilder
import com.tokopedia.play.robot.Robot
import com.tokopedia.play.robot.RobotWithValue
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
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PiPState
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.action.ClickCloseLeaderboardSheetAction
import com.tokopedia.play.view.uimodel.action.ClickLikeAction
import com.tokopedia.play.view.uimodel.action.InteractiveWinnerBadgeClickedAction
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.PlayViewerNewUiEvent
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayLiveRoomMetricsCommon
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelRobot(
    channelId: String,
    private val playVideoBuilder: PlayVideoWrapper.Builder,
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
    getSocketCredentialUseCase: GetSocketCredentialUseCase,
    private val getReportSummariesUseCase: GetReportSummariesUseCase,
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
    playSocketToModelMapper: PlaySocketToModelMapper,
    playUiModelMapper: PlayUiModelMapper,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers,
    remoteConfig: RemoteConfig,
    playPreference: PlayPreference,
    videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
    playChannelWebSocket: PlayWebSocket,
    private val repo: PlayViewerRepository,
    playAnalytic: PlayNewAnalytic,
    timerFactory: TimerFactory,
    castPlayerHelper: CastPlayerHelper,
    playShareExperience: PlayShareExperience,
    chatManagerFactory: ChatManager.Factory,
    chatStreamsFactory: ChatStreams.Factory,
    playLog: PlayLog,
    liveRoomMetricsCommon: PlayLiveRoomMetricsCommon,
) : Robot {

    private val productTagBuilder = PlayProductTagsModelBuilder()

    val viewModel: PlayViewModel = PlayViewModel(
        channelId,
        playVideoBuilder,
        videoStateProcessorFactory,
        channelStateProcessorFactory,
        videoBufferGovernorFactory,
        getReportSummariesUseCase,
        playSocketToModelMapper,
        playUiModelMapper,
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
        liveRoomMetricsCommon,
    )

    fun createPage(channelData: PlayChannelData) {
        viewModel.createPage(channelData)
    }

    fun focusPage(channelData: PlayChannelData) {
        viewModel.focusPage(channelData)
    }

    fun defocusPage(shouldPauseVideo: Boolean) {
        viewModel.defocusPage(shouldPauseVideo)
    }

    fun setMockResponseReportSummaries(response: ReportSummaries) {
        coEvery { getReportSummariesUseCase.executeOnBackground() } returns response
    }

    fun setMockResponseIsLike(response: Boolean) {
        coEvery { repo.getIsLiked(any(), any()) } returns response
    }

    fun setMockUserId(userId: String) {
        every { userSession.userId } returns userId
    }

    fun setPiPState(pipState: PiPState) {
        when(pipState) {
            is PiPState.Requesting -> when (val mode = pipState.mode) {
                PiPMode.WatchInPiP -> viewModel.requestWatchInPiP()
                is PiPMode.BrowsingOtherPage -> viewModel.requestPiPBrowsingPage(mode.applinkModel)
                else -> {}
            }
            is PiPState.InPiP -> {
                when (val mode = pipState.mode) {
                    PiPMode.WatchInPiP -> viewModel.requestWatchInPiP()
                    is PiPMode.BrowsingOtherPage -> viewModel.requestPiPBrowsingPage(mode.applinkModel)
                    else -> {}
                }
                viewModel.goPiP()
            }
            PiPState.Stop -> viewModel.stopPiP()
        }.exhaustive
    }

    fun showKeyboard(keyboardHeight: Int = 50) {
        viewModel.onKeyboardShown(keyboardHeight)
    }

    fun hideKeyboard() {
        viewModel.onKeyboardHidden()
    }

    fun showProductBottomSheet(bottomSheetHeight: Int = 50) {
        viewModel.onShowProductSheet(bottomSheetHeight)
    }

    fun showCouponBottomSheet(bottomSheetHeight: Int = 50){
        viewModel.showCouponSheet(bottomSheetHeight)
    }

    fun showKebabBottomSheet(bottomSheetHeight: Int = 20){
        viewModel.onShowKebabMenuSheet(bottomSheetHeight)
    }

    fun showUserReportBottomSheet(bottomSheetHeight: Int = 80){
        viewModel.onShowUserReportSheet(bottomSheetHeight)
    }

    fun showUserReportSubmissionBottomSheet(bottomSheetHeight: Int = 80){
        viewModel.onShowUserReportSubmissionSheet(bottomSheetHeight)
    }

    fun hideProductBottomSheet() {
        viewModel.onHideProductSheet()
    }

    fun showVariantBottomSheet(bottomSheetHeight: Int = 50, action: ProductAction = ProductAction.Buy, product: PlayProductUiModel.Product = productTagBuilder.buildProductLine()) {
        viewModel.onShowVariantSheet(bottomSheetHeight)
    }

    fun hideVariantBottomSheet() {
        viewModel.onHideVariantSheet()
    }

    fun showLeaderboardBottomSheet(bottomSheetHeight: Int = 50) {
        viewModel.submitAction(InteractiveWinnerBadgeClickedAction(bottomSheetHeight))
    }

    fun hideLeaderboardBottomSheet() {
        viewModel.submitAction(ClickCloseLeaderboardSheetAction)
    }

    fun hideCouponBottomSheet(){
        viewModel.hideCouponSheet()
    }

    fun hideKebabBottomSheet(){
        viewModel.hideKebabMenuSheet()
    }

    fun hideUserReportBottomSheet(){
        viewModel.hideUserReportSheet()
    }

    fun hideUserReportSubmissionBottomSheet(){
        viewModel.hideUserReportSubmissionSheet()
    }

    fun goBack() = viewModel.goBack()

    fun setMockPlayer(player: PlayVideoWrapper) {
        every { playVideoBuilder.build() } returns player
    }

    fun getVideoPlayer() = viewModel.getVideoPlayer()

    fun setLoggedIn(isUserLoggedIn: Boolean) {
        every { userSession.isLoggedIn } returns isUserLoggedIn
    }

    fun setName(name: String) {
        every { userSession.name } returns name
    }

    fun setUserId(userId: String) {
        every { userSession.userId } returns userId
    }

    fun sendChat(message: String) {
        viewModel.sendChat(message)
    }

    fun doLike() {
        submitAction(ClickLikeAction)
    }

    fun doUnlike() {
        submitAction(ClickLikeAction)
    }

    fun isPiPAllowed() = viewModel.isPiPAllowed

    fun submitAction(action: PlayViewerNewAction) {
        viewModel.submitAction(action)
    }
}

fun givenPlayViewModelRobot(
    channelId: String = "1",
    playVideoBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true),
    videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory = mockk(relaxed = true),
    channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory = mockk(relaxed = true),
    videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    getReportSummariesUseCase: GetReportSummariesUseCase = mockk(relaxed = true),
    trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
    playSocketToModelMapper: PlaySocketToModelMapper = mockk(relaxed = true),
    playUiModelMapper: PlayUiModelMapper = ClassBuilder().getPlayUiModelMapper(),
    userSession: UserSessionInterface = mockk(relaxed = true),
    dispatchers: CoroutineDispatchers = CoroutineTestDispatchers,
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
    fn: PlayViewModelRobot.() -> Unit = {},
): PlayViewModelRobot {
    return PlayViewModelRobot(
        channelId = channelId,
        playVideoBuilder = playVideoBuilder,
        videoStateProcessorFactory = videoStateProcessorFactory,
        channelStateProcessorFactory = channelStateProcessorFactory,
        videoBufferGovernorFactory = videoBufferGovernorFactory,
        getSocketCredentialUseCase = getSocketCredentialUseCase,
        getReportSummariesUseCase = getReportSummariesUseCase,
        trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
        playSocketToModelMapper = playSocketToModelMapper,
        playUiModelMapper = playUiModelMapper,
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
        liveRoomMetricsCommon = liveRoomMetricsCommon,
    ).apply(fn)
}

suspend fun PlayViewModelRobot.state() = viewModel.uiState.first()

fun PlayViewModelRobot.withState(
        dispatcher: CoroutineTestDispatchers = CoroutineTestDispatchers,
        fn: suspend PlayViewerNewUiState.() -> Unit
) = runBlockingTest(dispatcher.coroutineDispatcher) {
    state().fn()
}

/**
 * Temporary. might need to use Turbine library
 */
infix fun PlayViewModelRobot.andWhenExpectEvent(
        fn: PlayViewModelRobot.() -> Unit
) : RobotWithValue<PlayViewModelRobot, PlayViewerNewUiEvent> {
    var result: PlayViewerNewUiEvent? = null
    runBlockingTest {
        val value = async {
            viewModel.uiEvent.first()
        }

        fn()

        result = value.await()
    }
    return RobotWithValue(this, result!!)
}
