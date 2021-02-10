package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.play.data.ChannelStatusResponse
import com.tokopedia.play.data.ReportSummaries
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.*
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.channel.state.PlayViewerChannelStateProcessor
import com.tokopedia.play.util.video.buffer.PlayViewerVideoBufferGovernor
import com.tokopedia.play.util.video.state.PlayViewerVideoStateProcessor
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.mapper.PlaySocketToModelMapper
import com.tokopedia.play.view.uimodel.mapper.PlayUiModelMapper
import com.tokopedia.play.view.uimodel.recom.LikeSource
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.play_common.player.PlayVideoWrapper
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by jegul on 20/02/20
 */
class PlayViewModelCreatePageTest {

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
    fun `given channel data is set, when page is created, then video stream value should be the same as in channel data`() {
        val id = "1"
        val videoOrientation = VideoOrientation.Horizontal(1, 2)

        val channelData = channelDataBuilder.buildChannelData(
                videoMetaInfo = videoModelBuilder.buildVideoMeta(
                        videoStream = videoModelBuilder.buildVideoStream(
                                id = id,
                                orientation = videoOrientation
                        )
                )
        )
        val expectedModel = videoModelBuilder.buildVideoStream(
                id = id,
                orientation = videoOrientation
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableVideoMeta.getOrAwaitValue().videoStream

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then like status value should be the same as in channel data`() {
        val totalLike = 5
        val totalLikeFormatted = totalLike.toString()
        val isLiked = false

        val channelData = channelDataBuilder.buildChannelData(
                likeInfo = likeBuilder.buildCompleteData(
                        status = likeBuilder.buildStatus(
                                totalLike = totalLike,
                                totalLikeFormatted = totalLikeFormatted,
                                isLiked = isLiked
                        )
                )
        )
        val expectedModel = likeBuilder.buildStatus(
                totalLike = totalLike,
                totalLikeFormatted = totalLikeFormatted,
                isLiked = isLiked,
                source = LikeSource.Storage
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableLikeStatusInfo.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then total view value should be the same as in channel data`() {
        val totalView = "5.8k"

        val channelData = channelDataBuilder.buildChannelData(
                totalViewInfo = totalViewBuilder.buildCompleteData(
                        totalView = totalView
                )
        )

        val expectedModel = totalViewBuilder.buildCompleteData(
                totalView = totalView
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableTotalViews.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then partner info value should be the same as in channel data`() {
        val partnerName = "Penjual Sakti"
        val isFollowed = true

        val channelData = channelDataBuilder.buildChannelData(
                partnerInfo = partnerInfoBuilder.buildCompleteData(
                        basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(
                                name = partnerName
                        ),
                        followInfo = partnerInfoBuilder.buildPlayPartnerFollowInfo(
                                isFollowed = isFollowed
                        )
                )
        )

        val expectedModel = partnerInfoBuilder.buildCompleteData(
                basicInfo = partnerInfoBuilder.buildPlayPartnerBasicInfo(name = partnerName),
                followInfo = partnerInfoBuilder.buildPlayPartnerFollowInfo(isFollowed = isFollowed)
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observablePartnerInfo.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then cart info value should be the same as in channel data`() {
        val shouldShowCart = true
        val itemInCartCount = 95

        val channelData = channelDataBuilder.buildChannelData(
                cartInfo = cartInfoBuilder.buildCompleteData(
                        shouldShow = shouldShowCart,
                        count = itemInCartCount
                )
        )

        val expectedModel = cartInfoBuilder.buildCompleteData(
                shouldShow = shouldShowCart,
                count = itemInCartCount
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableCartInfo.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then quick replies value should be the same as in channel data`() {
        val quickReplyList = listOf("Wah keren", "Bagus Sekali", "<3")

        val channelData = channelDataBuilder.buildChannelData(
                quickReplyInfo = quickReplyBuilder.build(quickReplyList)
        )

        val expectedModel = quickReplyBuilder.build(quickReplyList)

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableQuickReply.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then share info value should be the same as in channel data`() {
        val shareContent = "Ayo buruan beli sekarang hanya di https://www.tokopedia.com"
        val shouldShowShare = shareContent.isNotEmpty()

        val channelData = channelDataBuilder.buildChannelData(
                shareInfo = shareInfoBuilder.build(
                        content = shareContent,
                        shouldShow = shouldShowShare
                )
        )

        val expectedModel = shareInfoBuilder.build(
                content = shareContent,
                shouldShow = shouldShowShare
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableShareInfo.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then channel info value should be the same as in channel data`() {
        val channelType = PlayChannelType.VOD
        val backgroundUrl = "https://tokopedia.com/play/channels"

        val channelData = channelDataBuilder.buildChannelData(
                channelInfo = channelInfoBuilder.buildChannelInfo(
                        channelType = channelType,
                        backgroundUrl = backgroundUrl
                )
        )

        val expectedModel = channelInfoBuilder.buildChannelInfo(
                channelType = channelType,
                backgroundUrl = backgroundUrl
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observableChannelInfo.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given channel data is set, when page is created, then pinned value should be the same as in channel data`() {
        val pinnedMessage = "Saksikan keseruan BTS di sini"
        val shouldShowPinnedProduct = pinnedMessage.isEmpty()

        val channelData = channelDataBuilder.buildChannelData(
                pinnedInfo = pinnedBuilder.buildInfo(
                        pinnedMessage = pinnedBuilder.buildPinnedMessage(
                                title = pinnedMessage
                        ),
                        pinnedProduct = pinnedBuilder.buildPinnedProduct(
                                shouldShow = shouldShowPinnedProduct
                        )
                )
        )

        val expectedModel = pinnedBuilder.buildPinnedMessage(
                title = pinnedMessage
        )

        playViewModel.createPage(channelData)

        val actualModel = playViewModel.observablePinned.getOrAwaitValue()

        Assertions
                .assertThat(actualModel)
                .isEqualTo(expectedModel)
    }
}