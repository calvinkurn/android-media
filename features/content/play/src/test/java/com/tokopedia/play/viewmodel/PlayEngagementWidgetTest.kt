package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.*
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.ui.engagement.model.EngagementUiModel
import com.tokopedia.play.util.*
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.websocket.response.PlayMerchantVoucherSocketResponse
import com.tokopedia.play_common.model.dto.interactive.InteractiveUiModel
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.play_common.websocket.WebSocketAction
import com.tokopedia.play_common.websocket.WebSocketResponse
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 12/09/22
 */
@ExperimentalCoroutinesApi
class PlayEngagementWidgetTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val videoBuilder = PlayVideoModelBuilder()
    private val mockChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(channelType = PlayChannelType.Live),
        ),
        videoMetaInfo = videoBuilder.buildVideoMeta(
            videoPlayer = videoBuilder.buildCompleteGeneralVideoPlayer(),
            videoStream = videoBuilder.buildVideoStream(orientation = VideoOrientation.Vertical)
        )
    )

    private val testDispatcher = coroutineTestRule.dispatchers

    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val repo: PlayViewerRepository = mockk(relaxed = true)
    private val socket: PlayWebSocket = mockk(relaxed = true)
    private val socketResponseBuilder = PlaySocketResponseBuilder()
    private val socketFlow = MutableStateFlow<WebSocketAction>(
        WebSocketAction.NewMessage(
            socketResponseBuilder.buildChannelInteractiveResponse(isExist = true)
        )
    )
    private val vouchers = PlayMerchantVoucherSocketResponse.generateResponse(
        isPrivate = false
    )

    private val mockVoucherSocketResponse = Gson().fromJson(
        vouchers,
        WebSocketResponse::class.java
    )

    init {
        every { mockRemoteConfig.getBoolean(any(), any()) } returns true
    }

    //add each test for game
    //check if voucher list for coupon not empty
    //click voucher open coupon sheet
    //close voucher

    /**
     * Voucher
     */
    @Test
    fun `voucher - when empty hide`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.tagItems.voucher.voucherList.assertEmpty()
            state.engagement.data.assertEmpty()
            state.engagement.shouldShow.assertFalse()
        }
    }

    @Test
    fun `voucher only - exist`() {
        every { socket.listenAsFlow() } returns socketFlow

        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
            playChannelWebSocket = socket
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {
                socketFlow.emit(WebSocketAction.NewMessage(mockVoucherSocketResponse))
            }
            state.engagement.data.first().assertType<EngagementUiModel.Promo> { }
            state.engagement.data.size.assertEqualTo(1)
            state.engagement.shouldShow.assertTrue()
        }
    }

    @Test
    fun `voucher is exist but no highlighted voucher` () {
        coEvery { repo.getTagItem(any(), any(), any()) } returns uiModelBuilder.buildTagItem(
            voucher = uiModelBuilder.buildVoucherModel(
                voucherList = listOf(uiModelBuilder.buildMerchantVoucher())
            )
        )

        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.engagement.data.assertEmpty()
            state.engagement.shouldShow.assertFalse()

        }
    }

    /**
     * Game
     */
    @Test
    fun `game - when empty hide`() {
        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.interactive.interactive.assertInstanceOf<InteractiveUiModel.Unknown>()
            state.engagement.data.assertEmpty()
            state.engagement.shouldShow.assertFalse()
        }
    }

    @Test
    fun `game - when only game exist`() {
        every { socket.listenAsFlow() } returns socketFlow

        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            status = InteractiveUiModel.Giveaway.Status.Ongoing(
                200L.millisFromNow()
            )
        )

        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
            playChannelWebSocket = socket
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {}
            state.interactive.interactive.assertInstanceOf<InteractiveUiModel.Giveaway>()
            state.engagement.data.first().assertType<EngagementUiModel.Game> { dt ->
                dt.interactive.assertType<InteractiveUiModel.Giveaway> { }
            }
            state.engagement.data.size.assertEqualTo(1)
            state.engagement.shouldShow.assertTrue()
        }
    }

    /**
     * Both
     */

    @Test
    fun `game and engagement - exist`() {
        every { socket.listenAsFlow() } returns socketFlow

        coEvery { repo.getCurrentInteractive(any()) } returns uiModelBuilder.buildGiveaway(
            status = InteractiveUiModel.Giveaway.Status.Ongoing(
                200L.millisFromNow()
            )
        )

        createPlayViewModelRobot(
            repo = repo,
            dispatchers = testDispatcher,
            remoteConfig = mockRemoteConfig,
            playChannelWebSocket = socket
        ).use {
            it.createPage(mockChannelData)
            it.focusPage(mockChannelData)

            val state = it.recordState {
                socketFlow.emit(WebSocketAction.NewMessage(mockVoucherSocketResponse))
            }
            state.interactive.interactive.assertInstanceOf<InteractiveUiModel.Giveaway>()
            state.engagement.data.first().assertType<EngagementUiModel.Game> { dt ->
                dt.interactive.assertType<InteractiveUiModel.Giveaway> { }
            }
            state.engagement.data.last().assertType<EngagementUiModel.Promo> { }
            state.engagement.shouldShow.assertTrue()
        }
    }
}
