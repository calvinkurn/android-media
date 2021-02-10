package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.data.ReportSummaries
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.*
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 09/02/21
 */
class PlayViewModelUpdatePageTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockPlayVideo: PlayVideoWrapper.Builder = mockk(relaxed = true)
    private val mockPlayVideoStateProcessorFactory: PlayViewerVideoStateProcessor.Factory = mockk(relaxed = true)
    private val mockPlayChannelStateProcessorFactory: PlayViewerChannelStateProcessor.Factory = mockk(relaxed = true)
    private val mockPlayVideoBufferGovernorFactory: PlayViewerVideoBufferGovernor.Factory = mockk(relaxed = true)
    private val mockGetChannelStatusUseCase: GetChannelStatusUseCase = mockk(relaxed = true)
    private val mockGetSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true)
    private val mockGetPartnerInfoUseCase: GetPartnerInfoUseCase = mockk(relaxed = true)
    private val mockGetReportSummariesUseCase: GetReportSummariesUseCase = mockk(relaxed = true)
    private val mockGetIsLikeUseCase: GetIsLikeUseCase = mockk(relaxed = true)
    private val mockGetCartCountUseCase: GetCartCountUseCase = mockk(relaxed = true)
    private val mockGetProductTagItemsUseCase: GetProductTagItemsUseCase = mockk(relaxed = true)
    private val mockTrackProductTagBroadcasterUseCase: TrackProductTagBroadcasterUseCase = mockk(relaxed = true)
    private val mockTrackVisitChannelBroadcasterUseCase: TrackVisitChannelBroadcasterUseCase = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val mockPlaySocket: PlaySocket = mockk(relaxed = true)
    private val mockPlaySocketToModelMapper: PlaySocketToModelMapper = mockk(relaxed = true)
    private val mockPlayUiModelMapper: PlayUiModelMapper = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val pinnedBuilder = PlayPinnedModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()
    private val shareInfoBuilder = PlayShareInfoModelBuilder()
    private val quickReplyBuilder = PlayQuickReplyModelBuilder()
    private val cartInfoBuilder = PlayCartInfoModelBuilder()
    private val partnerInfoBuilder = PlayPartnerInfoModelBuilder()
    private val totalViewBuilder = PlayTotalViewModelBuilder()
    private val likeBuilder = PlayLikeModelBuilder()
    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val videoModelBuilder = PlayVideoModelBuilder()

    private val responseBuilder = PlayResponseBuilder()

    private lateinit var playViewModel: PlayViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatchers.main)

        playViewModel = PlayViewModel(
                mockPlayVideo,
                mockPlayVideoStateProcessorFactory,
                mockPlayChannelStateProcessorFactory,
                mockPlayVideoBufferGovernorFactory,
                mockGetChannelStatusUseCase,
                mockGetSocketCredentialUseCase,
                mockGetPartnerInfoUseCase,
                mockGetReportSummariesUseCase,
                mockGetIsLikeUseCase,
                mockGetCartCountUseCase,
                mockGetProductTagItemsUseCase,
                mockTrackProductTagBroadcasterUseCase,
                mockTrackVisitChannelBroadcasterUseCase,
                mockPlaySocket,
                mockPlaySocketToModelMapper,
                mockPlayUiModelMapper,
                userSession,
                dispatchers,
                mockk(relaxed = true),
                mockk(relaxed = true)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given channel data is set, when page is focused, then like info should be updated accordingly`() {
        val isLiked = true
        coEvery { mockGetReportSummariesUseCase.executeOnBackground() } returns responseBuilder.buildReportSummariesResponse()
        coEvery { mockGetIsLikeUseCase.executeOnBackground() } returns isLiked

        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeBuilder.buildCompleteData(
                        status = likeBuilder.buildStatus(isLiked = false)
                )
        )
        val expectedModel = likeBuilder.buildStatus(
                isLiked = isLiked
        )

        playViewModel.createPage(channelData)
        playViewModel.focusPage(channelData)

        val actualModel = playViewModel.observableLikeStatusInfo.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }
}