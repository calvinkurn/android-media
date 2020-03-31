package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by jegul on 20/02/20
 */
class PlayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockPlayVideoManager: PlayVideoManager = mockk(relaxed = true)
    private val mockGetChannelInfoUseCase: GetChannelInfoUseCase = mockk(relaxed = true)
    private val mockGetPartnerInfoUseCase: GetPartnerInfoUseCase = mockk(relaxed = true)
    private val mockGetTotalLikeUseCase: GetTotalLikeUseCase = mockk(relaxed = true)
    private val mockGetIsLikeUseCase: GetIsLikeUseCase = mockk(relaxed = true)
    private val mockGetCartCountUseCase: GetCartCountUseCase = mockk(relaxed = true)
    private val mockGetProductTagItemsUseCase: GetProductTagItemsUseCase = mockk(relaxed = true)
    private val mockPlaySocket: PlaySocket = mockk(relaxed = true)
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val modelBuilder = ModelBuilder()

    private val mockChannel = modelBuilder.buildChannel()
    private val mockShopInfo = modelBuilder.buildShopInfo()
    private val mockNewChat = modelBuilder.buildNewChat()
    private val mockCartCount = 1
    private val mockProductTagging = modelBuilder.buildProductTagging()


    private val mockTotalLikeContentData = modelBuilder.buildTotalLike()
    private val mockTotalLike = TotalLike(mockTotalLikeContentData.like.value, mockTotalLikeContentData.like.fmt)

    private val mockIsLikeContentData = modelBuilder.buildIsLike()
    private val mockIsLike = mockIsLikeContentData.isLike

    private lateinit var playViewModel: PlayViewModel

    @Before
    fun setUp() {
        playViewModel = PlayViewModel(
                mockPlayVideoManager,
                mockGetChannelInfoUseCase,
                mockGetPartnerInfoUseCase,
                mockGetTotalLikeUseCase,
                mockGetIsLikeUseCase,
                mockGetCartCountUseCase,
                mockGetProductTagItemsUseCase,
                mockPlaySocket,
                userSession,
                dispatchers
        )

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetPartnerInfoUseCase.executeOnBackground() } returns mockShopInfo
        coEvery { mockGetTotalLikeUseCase.executeOnBackground() } returns mockTotalLike
        coEvery { mockGetIsLikeUseCase.executeOnBackground() } returns mockIsLike
        coEvery { mockGetCartCountUseCase.executeOnBackground() } returns mockCartCount
        coEvery { mockGetProductTagItemsUseCase.executeOnBackground() } returns mockProductTagging
        coEvery { mockPlaySocket.channelId } returns ""
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `test observe video play manager`() {
        val mockExoPlayer: SimpleExoPlayer = mockk()
        val mockObservableVideoPlayer = MutableLiveData<ExoPlayer>().apply {
            value = mockExoPlayer
        }

        every { mockPlayVideoManager.getObservableVideoPlayer() } returns mockObservableVideoPlayer

        Assertions
                .assertThat(playViewModel.observableVOD.getOrAwaitValue())
                .isEqualTo(mockExoPlayer)
    }

    @Test
    fun `test observe get channel info`() {
        val expectedModel = ChannelInfoUiModel(
                id = mockChannel.channelId,
                title = mockChannel.title,
                description = mockChannel.description,
                channelType = if (mockChannel.videoStream.isLive) PlayChannelType.Live else PlayChannelType.VOD,
                moderatorName = mockChannel.moderatorName,
                partnerId = mockChannel.partnerId,
                partnerType = PartnerType.getTypeByValue(mockChannel.partnerType),
                contentId = mockChannel.contentId,
                contentType = mockChannel.contentType,
                likeType = mockChannel.likeType,
                isShowCart = mockChannel.isShowCart
        )
        val expectedResult = Success(expectedModel)

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableGetChannelInfo.getOrAwaitValue())
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe video stream`() {
        val expectedModel = VideoStreamUiModel(
                uriString = mockChannel.videoStream.config.streamUrl,
                channelType = if (mockChannel.videoStream.isLive &&
                        mockChannel.videoStream.type.equals(PlayChannelType.Live.value, true))
                    PlayChannelType.Live else PlayChannelType.VOD,
                isActive = mockChannel.isActive
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableVideoStream.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe total likes`() {
        val expectedModel = TotalLikeUiModel(
                totalLike = mockTotalLike.totalLike,
                totalLikeFormatted = mockTotalLike.totalLikeFormatted
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe is liked`() {
        val expectedModel = mockIsLike

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableIsLikeContent.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe total views`() {
        val expectedModel = TotalViewUiModel(
                totalView = mockChannel.totalViews
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableTotalViews.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe partner info`() {
        val expectedModel = PartnerInfoUiModel(
                id = mockChannel.partnerId,
                name = mockChannel.moderatorName,
                type = PartnerType.ADMIN,
                isFollowed = true,
                isFollowable = false
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observablePartnerInfo.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe badge cart`() {
        val expectedModel = CartUiModel(
                count = 1,
                isShow = true
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableBadgeCart.getOrAwaitValue())
                .isEqualTo(expectedModel)

    }

    @Test
    fun `test observe product tagging`() {
        val expectedModel = modelBuilder.buildProductTagging()
        val expectedResult = PlayResult.Success(
                PlayUiMapper.mapProductSheet(
                        mockChannel.pinnedProduct.titleBottomSheet,
                        expectedModel)
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableProductSheetContent.getOrAwaitValue())
                .isEqualTo(expectedResult)

    }
}