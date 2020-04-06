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
import com.tokopedia.play.ui.chatlist.model.PlayChat
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
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

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
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val mockPlaySocket: PlaySocket = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val modelBuilder = ModelBuilder()

    private val mockChannel = modelBuilder.buildChannel()
    private val mockShopInfo = modelBuilder.buildShopInfo()
    private val mockNewChat = modelBuilder.buildNewChat()

    private val mockTotalLikeContentData = modelBuilder.buildTotalLike()
    private val mockTotalLike = TotalLike(mockTotalLikeContentData.like.value, mockTotalLikeContentData.like.fmt)

    private val mockIsLikeContentData = modelBuilder.buildIsLike()
    private val mockIsLike = mockIsLikeContentData.isLike

    private val mockCartCount = 1
    private val mockProductTagging = modelBuilder.buildProductTagging()

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

    @Nested
    @DisplayName("Type of Partner Info")
    inner class TypeOfPartnerInfo {

        @Test
        fun `test observe partner info when partner type is admin`() {
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
        fun `test observe partner info when partner type is shop owner`() {
            val mockChannel = modelBuilder.buildChannelWithShop()

            coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel
            coEvery { userSession.shopId } returns mockChannel.partnerId.toString()

            val expectedModel = PartnerInfoUiModel(
                    id = mockShopInfo.shopCore.shopId.toLong(),
                    name = mockShopInfo.shopCore.name,
                    type = PartnerType.SHOP,
                    isFollowed = mockShopInfo.favoriteData.alreadyFavorited == 1,
                    isFollowable = userSession.shopId != mockShopInfo.shopCore.shopId
            )

            playViewModel.getChannelInfo(mockChannel.channelId)

            Assertions
                    .assertThat(playViewModel.observablePartnerInfo.getOrAwaitValue())
                    .isEqualTo(expectedModel)
        }
    }

    @Test
    fun `test observe badge cart`() {
        val expectedModel = CartUiModel(
                count = mockCartCount,
                isShow = true
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableBadgeCart.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test update badge cart`() {
        val expectedModel = CartUiModel(
                isShow = true,
                count = mockCartCount
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.updateBadgeCart()

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

    @Test
    fun `test send chat through socket`() {

        coEvery { userSession.isLoggedIn } returns true
        coEvery { userSession.userId } returns "123"
        coEvery { userSession.name } returns "name"
        coEvery { userSession.profilePicture } returns "picture"

        val messages = "mock chat"
        val expectedModel = PlayUiMapper.mapPlayChat(userSession.userId,
                PlayChat(
                        message = messages,
                        user = PlayChat.UserData(
                                id = userSession.userId,
                                name = userSession.name,
                                image = userSession.profilePicture)
                ))

        playViewModel.sendChat(messages)

        Assertions
                .assertThat(playViewModel.observableNewChat.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test change like count`() {
        val expectedModel = TotalLikeUiModel(
                totalLike = mockTotalLike.totalLike + 1,
                totalLikeFormatted = (mockTotalLike.totalLike + 1).toString()
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.changeLikeCount(true)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe quick reply`() {
        val expectedModel = QuickReplyUiModel(
                quickReplyList = mockChannel.quickReply
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableQuickReply.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `test observe channel type`() {
        val expectedResult =
                if (mockChannel.videoStream.isLive)
                    PlayChannelType.Live
                else
                    PlayChannelType.VOD

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe channel type when null`() {

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = PlayChannelType.Unknown

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe content id`() {
        val expectedResult = mockChannel.contentId

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentId)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe content id when null`() {

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = 0

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentId)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe content type`() {
        val expectedResult = mockChannel.contentType

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe content type when null`() {

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = 0

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe like type`() {
        val expectedResult = mockChannel.likeType

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.likeType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe like type when null`() {

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = 0

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.likeType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe partner id`() {
        val expectedResult = mockChannel.partnerId

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.partnerId)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe partner id when null`() {

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = null

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.partnerId)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe total view`() {
        val expectedResult = mockChannel.totalViews

        playViewModel.getChannelInfo(mockChannel.totalViews)

        Assertions
                .assertThat(playViewModel.totalView)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `test observe total view when null`() {

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = null

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.totalView)
                .isEqualTo(expectedResult)
    }
}