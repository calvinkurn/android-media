package com.tokopedia.play.robot.play

import com.tokopedia.play.data.ReportSummaries
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.play.data.websocket.PlayChannelWebSocket
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.helper.ClassBuilder
import com.tokopedia.play.model.PlayProductTagsModelBuilder
import com.tokopedia.play.robot.play.result.PlayViewModelRobotResult
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.monitoring.PlayVideoLatencyPerformanceMonitoring
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.type.PiPMode
import com.tokopedia.play.view.type.PiPState
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.util.extension.exhaustive
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

/**
 * Created by jegul on 10/02/21
 */
typealias RobotWithValue<T> = Pair<PlayViewModelRobot, T>

class PlayViewModelRobot(
        private val playVideoBuilder: PlayVideoWrapper.Builder,
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
        dispatchers: CoroutineDispatchers,
        remoteConfig: RemoteConfig,
        playPreference: PlayPreference,
        videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring,
        playChannelWebSocket: PlayChannelWebSocket,
) {

    private val productTagBuilder = PlayProductTagsModelBuilder()

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
                remoteConfig,
                playPreference,
                videoLatencyPerformanceMonitoring,
                playChannelWebSocket
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

    fun updateCartCountFromNetwork() {
        viewModel.updateBadgeCart()
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

    fun hideProductBottomSheet() {
        viewModel.onHideProductSheet()
    }

    fun showVariantBottomSheet(bottomSheetHeight: Int = 50, action: ProductAction = ProductAction.Buy, product: PlayProductUiModel.Product = productTagBuilder.buildProductLine()) {
        viewModel.onShowVariantSheet(bottomSheetHeight, action = action, product = product)
    }

    fun hideVariantBottomSheet() {
        viewModel.onHideVariantSheet()
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
        viewModel.changeLikeCount(true)
    }

    fun doUnlike() {
        viewModel.changeLikeCount(false)
    }

    fun isPiPAllowed() = viewModel.isPiPAllowed
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
        dispatchers: CoroutineDispatchers = CoroutineTestDispatchers,
        remoteConfig: RemoteConfig = mockk(relaxed = true),
        playPreference: PlayPreference = mockk(relaxed = true),
        videoLatencyPerformanceMonitoring: PlayVideoLatencyPerformanceMonitoring = mockk(relaxed = true),
        playChannelWebSocket: PlayChannelWebSocket = mockk(relaxed = true),
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
            remoteConfig = remoteConfig,
            playPreference = playPreference,
            videoLatencyPerformanceMonitoring = videoLatencyPerformanceMonitoring,
            playChannelWebSocket = playChannelWebSocket,
    ).apply(fn)
}

infix fun <T> PlayViewModelRobot.andWhen(
        fn: PlayViewModelRobot.() -> T
): RobotWithValue<T> {
    return Pair(this, run(fn))
}

infix fun PlayViewModelRobot.andThen(
        fn: PlayViewModelRobot.() -> Unit
): PlayViewModelRobot {
    return apply(fn)
}

infix fun <T> RobotWithValue<T>.andThen(
        fn: PlayViewModelRobot.(T) -> Unit
): PlayViewModelRobot {
    return first.apply { fn(second) }
}

infix fun PlayViewModelRobot.thenVerify(
        fn: PlayViewModelRobotResult.() -> Unit
): PlayViewModelRobot {
    PlayViewModelRobotResult(viewModel).apply { fn() }
    return this
}

infix fun <T> RobotWithValue<T>.thenVerify(
        fn: PlayViewModelRobotResult.(T) -> Unit
): PlayViewModelRobot {
    PlayViewModelRobotResult(first.viewModel).apply { fn(second) }
    return first
}