package com.tokopedia.play.robot.play

import com.tokopedia.play.data.ReportSummaries
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.robot.play.result.PlayViewModelRobotResult
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.uimodel.mapper.*
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

/**
 * Created by jegul on 10/02/21
 */
class PlayViewModelRobot(
        playVideoBuilder: PlayVideoWrapper.Builder,
        videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory,
        channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory,
        videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory,
        getChannelStatusUseCase: GetChannelStatusUseCase,
        getSocketCredentialUseCase: GetSocketCredentialUseCase,
        private val getPartnerInfoUseCase: GetPartnerInfoUseCase,
        private val getReportSummariesUseCase: GetReportSummariesUseCase,
        private val getIsLikeUseCase: GetIsLikeUseCase,
        private val getCartCountUseCase: GetCartCountUseCase,
        getProductTagItemsUseCase: GetProductTagItemsUseCase,
        trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase,
        trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase,
        playSocket: PlaySocket,
        playSocketToModelMapper: PlaySocketToModelMapper,
        playUiModelMapper: PlayUiModelMapper,
        private val userSession: UserSessionInterface,
        dispatchers: CoroutineDispatcherProvider,
        pageMonitoring: PlayPltPerformanceCallback,
        remoteConfig: RemoteConfig
) {

    val viewModel: PlayViewModel

    init {
        viewModel = PlayViewModel(
                playVideoBuilder,
                videoStateProcessorFactory,
                channelStateProcessorFactory,
                videoBufferGovernorFactory,
                getChannelStatusUseCase,
                getSocketCredentialUseCase,
                getPartnerInfoUseCase,
                getReportSummariesUseCase,
                getIsLikeUseCase,
                getCartCountUseCase,
                getProductTagItemsUseCase,
                trackProductTagBroadcasterUseCase,
                trackVisitChannelBroadcasterUseCase,
                playSocket,
                playSocketToModelMapper,
                playUiModelMapper,
                userSession,
                dispatchers,
                pageMonitoring,
                remoteConfig
        )
    }

    fun createPage(channelData: PlayChannelData) {
        viewModel.createPage(channelData)
    }

    fun focusPage(channelData: PlayChannelData) {
        viewModel.focusPage(channelData)
    }

    fun setMockResponseReportSummaries(response: ReportSummaries) {
        coEvery { getReportSummariesUseCase.executeOnBackground() } returns response
    }

    fun setMockResponseIsLike(response: Boolean) {
        coEvery { getIsLikeUseCase.executeOnBackground() } returns response
    }

    fun setMockPartnerInfoResponse(response: ShopInfo) {
        coEvery { getPartnerInfoUseCase.executeOnBackground() } returns response
    }

    fun setMockCartCountResponse(response: Int) {
        coEvery { getCartCountUseCase.executeOnBackground() } returns response
    }

    fun setMockUserId(userId: String) {
        every { userSession.userId } returns userId
    }

    fun setPiPMode(pipMode: PiPMode) {
        when(pipMode) {
            PiPMode.WatchInPip -> viewModel.watchInPiP()
            PiPMode.BrowsingOtherPage -> viewModel.openPiPBrowsingPage()
            PiPMode.StopPip -> viewModel.stopPiP()
        }.exhaustive
    }

    fun updateCartCountFromNetwork() {
        viewModel.updateBadgeCart()
    }
}

fun givenPlayViewModelRobot(
        playVideoBuilder: PlayVideoWrapper.Builder = mockk(relaxed = true),
        videoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory = mockk(relaxed = true),
        channelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory = mockk(relaxed = true),
        videoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory = mockk(relaxed = true),
        getChannelStatusUseCase: GetChannelStatusUseCase = mockk(relaxed = true),
        getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
        getPartnerInfoUseCase: GetPartnerInfoUseCase = mockk(relaxed = true),
        getReportSummariesUseCase: GetReportSummariesUseCase = mockk(relaxed = true),
        getIsLikeUseCase: GetIsLikeUseCase = mockk(relaxed = true),
        getCartCountUseCase: GetCartCountUseCase = mockk(relaxed = true),
        getProductTagItemsUseCase: GetProductTagItemsUseCase = mockk(relaxed = true),
        trackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase = mockk(relaxed = true),
        trackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase = mockk(relaxed = true),
        playSocket: PlaySocket = mockk(relaxed = true),
        playSocketToModelMapper: PlaySocketToModelMapper = mockk(relaxed = true),
        playUiModelMapper: PlayUiModelMapper = ClassBuilder().getPlayUiModelMapper(),
        userSession: UserSessionInterface = mockk(relaxed = true),
        dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider,
        pageMonitoring: PlayPltPerformanceCallback = mockk(relaxed = true),
        remoteConfig: RemoteConfig = mockk(relaxed = true),
        fn: PlayViewModelRobot.() -> Unit = {}
): PlayViewModelRobot {
    return PlayViewModelRobot(
            playVideoBuilder = playVideoBuilder,
            videoStateProcessorFactory = videoStateProcessorFactory,
            channelStateProcessorFactory = channelStateProcessorFactory,
            videoBufferGovernorFactory = videoBufferGovernorFactory,
            getChannelStatusUseCase = getChannelStatusUseCase,
            getSocketCredentialUseCase = getSocketCredentialUseCase,
            getPartnerInfoUseCase = getPartnerInfoUseCase,
            getReportSummariesUseCase = getReportSummariesUseCase,
            getIsLikeUseCase = getIsLikeUseCase,
            getCartCountUseCase = getCartCountUseCase,
            getProductTagItemsUseCase = getProductTagItemsUseCase,
            trackProductTagBroadcasterUseCase = trackProductTagBroadcasterUseCase,
            trackVisitChannelBroadcasterUseCase = trackVisitChannelBroadcasterUseCase,
            playSocket = playSocket,
            playSocketToModelMapper = playSocketToModelMapper,
            playUiModelMapper = playUiModelMapper,
            userSession = userSession,
            dispatchers = dispatchers,
            pageMonitoring = pageMonitoring,
            remoteConfig = remoteConfig
    ).apply(fn)
}

infix fun PlayViewModelRobot.andWhen(
        fn: PlayViewModelRobot.() -> Unit
): PlayViewModelRobot {
    return apply(fn)
}

infix fun PlayViewModelRobot.andThen(
        fn: PlayViewModelRobot.() -> Unit
): PlayViewModelRobot {
    return apply(fn)
}

infix fun PlayViewModelRobot.thenVerify(
        fn: PlayViewModelRobotResult.() -> Unit
): PlayViewModelRobot {
    PlayViewModelRobotResult(viewModel).apply { fn() }
    return this
}