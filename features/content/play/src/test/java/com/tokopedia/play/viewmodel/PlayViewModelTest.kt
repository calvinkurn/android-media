package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.*
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.mapper.PlayUiMapper
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.player.PlayVideoManager
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

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
    fun `test observe get channel info`() {
        val expectedModel = ChannelInfoUiModel(
                id = mockChannel.channelId,
                title = mockChannel.title,
                description = mockChannel.description,
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
        val expectedModel = modelBuilder.buildVideoStreamUiModel(
                uriString = mockChannel.videoStream.config.streamUrl,
                channelType = if (mockChannel.videoStream.isLive &&
                        mockChannel.videoStream.type.equals(PlayChannelType.Live.value, true))
                    PlayChannelType.Live else PlayChannelType.VOD,
                isActive = mockChannel.isActive,
                orientation = VideoOrientation.getByValue(mockChannel.videoStream.orientation),
                backgroundUrl = mockChannel.backgroundUrl
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
        val expectedModel = modelBuilder.buildLikeStateUiModel(
                isLiked = mockIsLike,
                fromNetwork = true
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableLikeState.getOrAwaitValue())
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

    //region partner info
    @Test
    fun `when partner type is admin, then partner info should be of type admin`() {
        val expectedModel = PartnerInfoUiModel(
                id = mockChannel.partnerId,
                name = mockChannel.moderatorName,
                type = PartnerType.Admin,
                isFollowed = true,
                isFollowable = false
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observablePartnerInfo.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `when partner type is not admin, then partner info should match the channel info partner type`() {
        val mockChannel = modelBuilder.buildChannelWithShop()

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel
        coEvery { userSession.shopId } returns mockChannel.partnerId.toString()

        val expectedModel = PartnerInfoUiModel(
                id = mockShopInfo.shopCore.shopId.toLong(),
                name = mockShopInfo.shopCore.name,
                type = PartnerType.Shop,
                isFollowed = mockShopInfo.favoriteData.alreadyFavorited == 1,
                isFollowable = userSession.shopId != mockShopInfo.shopCore.shopId
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observablePartnerInfo.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }
    //endregion

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

    //region update badge cart
    /**
     * Update badge cart
     */
    @Test
    fun `given channel info is success, when cart should show, then badge cart should be updated`() {
        val expectedModel = CartUiModel(
                isShow = true,
                count = mockCartCount
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.updateBadgeCart()

        coVerify {
            mockGetCartCountUseCase.executeOnBackground()
        }

        Assertions
                .assertThat(playViewModel.observableBadgeCart.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test(expected = TimeoutException::class)
    fun `given channel info is success, when cart should not show, then badge cart should not be updated`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(isShowCart = false)

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.updateBadgeCart()

        coVerify(exactly = 0) {
            mockGetCartCountUseCase.executeOnBackground()
        }

        playViewModel.observableBadgeCart.getOrAwaitValue()
    }

    @Test(expected = TimeoutException::class)
    fun `when channel info is not success, then badge cart should not be updated`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.updateBadgeCart()

        coVerify(exactly = 0) {
            mockGetCartCountUseCase.executeOnBackground()
        }

        playViewModel.observableBadgeCart.getOrAwaitValue()
    }

    @Test(expected = TimeoutException::class)
    fun `when channel info is null, then badge cart should not be updated`() {
        playViewModel.updateBadgeCart()

        coVerify(exactly = 0) {
            mockGetCartCountUseCase.executeOnBackground()
        }

        playViewModel.observableBadgeCart.getOrAwaitValue()
    }
    //endregion

    @Test
    fun `given product tag item use case is success, when get product tag item, then product sheet content should have correct value`() {
        val expectedModel = modelBuilder.buildProductTagging()
        val expectedResult = PlayResult.Success(
                PlayUiMapper.mapProductSheet(
                        mockChannel.pinnedProduct.titleBottomSheet,
                        mockChannel.partnerId,
                        expectedModel)
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.observableProductSheetContent.getOrAwaitValue())
                .isEqualTo(expectedResult)
    }

    @Test
    fun `given product tag item use case is error, when get product tag item, then product sheet content should be failure`() {
        val error = Exception("Error Get Product tag")

        coEvery { mockGetProductTagItemsUseCase.executeOnBackground() } throws error

        val expectedResult = PlayResult.Failure<ProductSheetUiModel>(
                error = error,
                onRetry = {}
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        val actual = playViewModel.observableProductSheetContent.getOrAwaitValue()

        Assertions
                .assertThat(actual)
                .isInstanceOf(PlayResult.Failure::class.java)

        Assertions
                .assertThat((actual as PlayResult.Failure).error)
                .isEqualTo(expectedResult.error)
    }

    //region send chat
    @Test
    fun `given user is logged in, when send chat, then there will be new chat that matches the chat sent`() {

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

        verifySequence { mockPlaySocket.send(messages) }

        Assertions
                .assertThat(playViewModel.observableNewChat.getOrAwaitValue().peekContent())
                .isEqualTo(expectedModel)
    }

    @Test(expected = TimeoutException::class)
    fun `given user is not logged in, when send chat, then no new chat is sent`() {

        coEvery { userSession.isLoggedIn } returns false
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

        verify(exactly = 0) { mockPlaySocket.send(messages) }

        playViewModel.observableNewChat.getOrAwaitValue()
    }
    //endregion

    //region like count
    @Test
    fun `given current like count is number only, when like count is added, then it should plus the like count by 1`() {
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
    fun `given current like count is null, when like count is added, then it should be 1`() {
        coEvery { mockGetTotalLikeUseCase.executeOnBackground() } throws Exception()

        val expectedModel = TotalLikeUiModel(
                totalLike = 1,
                totalLikeFormatted = 1.toString()
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.changeLikeCount(true)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given current like count contains string, when like count is added, then it should stay the same`() {
        val likeWithString = mockTotalLike.copy(
                totalLike = 1500,
                totalLikeFormatted = "1.5k"
        )

        coEvery { mockGetTotalLikeUseCase.executeOnBackground() } returns likeWithString

        val expectedModel = TotalLikeUiModel(
                totalLike = likeWithString.totalLike,
                totalLikeFormatted = likeWithString.totalLikeFormatted
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.changeLikeCount(true)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given current like count is number only, when like count is reduced, then it should reduce the like count by 1`() {
        val expectedModel = TotalLikeUiModel(
                totalLike = mockTotalLike.totalLike - 1,
                totalLikeFormatted = (mockTotalLike.totalLike - 1).toString()
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.changeLikeCount(false)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given current like count is null, when like count is reduced, then it should be 0`() {
        coEvery { mockGetTotalLikeUseCase.executeOnBackground() } throws Exception()

        val expectedModel = TotalLikeUiModel(
                totalLike = 0,
                totalLikeFormatted = 0.toString()
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.changeLikeCount(false)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }

    @Test
    fun `given current like count contains string, when like count is reduced, then it should stay the same`() {
        val likeWithString = mockTotalLike.copy(
                totalLike = 1500,
                totalLikeFormatted = "1.5k"
        )

        coEvery { mockGetTotalLikeUseCase.executeOnBackground() } returns likeWithString

        val expectedModel = TotalLikeUiModel(
                totalLike = likeWithString.totalLike,
                totalLikeFormatted = likeWithString.totalLikeFormatted
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.changeLikeCount(false)

        Assertions
                .assertThat(playViewModel.observableTotalLikes.getOrAwaitValue())
                .isEqualTo(expectedModel)
    }
    //endregion

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

    //region like type
    /**
     * Variable like type
     */
    @Test
    fun `when channel info is success, then like type should match with channel_info's like type`() {
        val expectedResult = mockChannel.likeType

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.likeType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is not success, then like type should be 0`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = 0

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.likeType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is null, then like type should be 0`() {
        val expectedResult = 0

        Assertions
                .assertThat(playViewModel.likeType)
                .isEqualTo(expectedResult)
    }
    //endregion

    //region content type
    /**
     * Variable content type
     */
    @Test
    fun `when channel info is success, then content type should match with channel_info's content type`() {
        val expectedResult = mockChannel.contentType

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is not success, then content type should be 0`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = 0

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is null, then content type should be 0`() {
        val expectedResult = 0

        Assertions
                .assertThat(playViewModel.contentType)
                .isEqualTo(expectedResult)
    }
    //endregion

    //region content id
    /**
     * Variable content id
     */
    @Test
    fun `when channel info is success, then content id should match with channel_info's content id`() {
        val expectedResult = mockChannel.contentId

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentId)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is not success, then content id should be 0`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = 0

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.contentId)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is null, then content id should be 0`() {
        val expectedResult = 0

        Assertions
                .assertThat(playViewModel.contentId)
                .isEqualTo(expectedResult)
    }
    //endregion

    //region bottom insets
    /**
     * Variable bottom insets
     */
    @Test
    fun `when no changes in insets before, bottom insets should be default`() {
        val expectedResult = modelBuilder.buildBottomInsetsMap()

        Assertions
                .assertThat(playViewModel.bottomInsets)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when there are changes in insets before, bottom insets should match the current state`() {
        val height = 250

        val expectedResult = modelBuilder.buildBottomInsetsMap(
                productSheetState = modelBuilder.buildBottomInsetsState(isShown = true, estimatedInsetsHeight = height)
        )

        playViewModel.onShowProductSheet(height)

        Assertions
                .assertThat(playViewModel.bottomInsets)
                .isEqualTo(expectedResult)
    }
    //endregion

    //region channel type
    /**
     * Variable channel type
     */
    @Test
    fun `when channel info is success, then channel type should match with channel_info's channel type`() {
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
    fun `when channel info is not success, then channel type should be unknown`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception("just throws")

        val expectedResult = PlayChannelType.Unknown

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is null, then channel type should be unknown`() {
        val expectedResult = PlayChannelType.Unknown

        Assertions
                .assertThat(playViewModel.channelType)
                .isEqualTo(expectedResult)
    }
    //endregion

    //region state helper
    /**
     * Variable state helper
     */
    @Test
    fun `when pinned is removed, then state helper should not show pinned`() {
        val expectedResult = false

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                pinnedMessage = mockChannel.pinnedMessage.copy(
                        pinnedMessageId = 0
                ),
                isShowProductTagging = false
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.observablePinned.getOrAwaitValue()

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).shouldShowPinned)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when there is pinned message only, then state helper should show pinned`() {
        val expectedResult = true

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                isShowProductTagging = false
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.observablePinned.getOrAwaitValue()

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).shouldShowPinned)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when there is pinned product only, then state helper should show pinned`() {
        val expectedResult = true

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                pinnedMessage = mockChannel.pinnedMessage.copy(
                        pinnedMessageId = 0
                )
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.observablePinned.getOrAwaitValue()

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).shouldShowPinned)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when there is pinned message and product, then state helper should show pinned`() {
        val expectedResult = true

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.observablePinned.getOrAwaitValue()

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).shouldShowPinned)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `given channel info is success, when channel type is live, then state_helper's channel type should be live`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.Live.isLive,
                        type = PlayChannelType.Live.value
                )
        )

        val expectedResult = PlayChannelType.Live

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `given channel info is success, when channel type is vod, then state_helper's channel type should be vod`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.VOD.isLive,
                        type = PlayChannelType.VOD.value
                )
        )

        val expectedResult = PlayChannelType.VOD

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is not success, then state_helper's channel type should be unknown`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } throws Exception()

        val expectedResult = PlayChannelType.Unknown

        playViewModel.getChannelInfo(mockChannel.channelId)

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when channel info is null, then state_helper's channel type should be unknown`() {
        val expectedResult = PlayChannelType.Unknown

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).channelType)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `given channel is live, when keyboard is shown, then state_helper's keyboard is shown`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.Live.isLive,
                        type = PlayChannelType.Live.value
                )
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.onKeyboardShown(123)

        val expectedResult = true

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).bottomInsets.isKeyboardShown)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `given channel is vod, when keyboard is shown, then state_helper's keyboard is shown`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.VOD.isLive,
                        type = PlayChannelType.VOD.value
                )
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.onKeyboardShown(123)

        val expectedResult = false

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).bottomInsets.isKeyboardShown)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when keyboard is not shown, then state_helper's keyboard is not shown`() {
        playViewModel.onKeyboardHidden()

        val expectedResult = false

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).bottomInsets.isKeyboardShown)
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when bottom insets is not set, then state_helper's keyboard is not shown`() {
        val expectedResult = false

        Assertions
                .assertThat(playViewModel.getStateHelper(ScreenOrientation.Portrait).bottomInsets.isKeyboardShown)
                .isEqualTo(expectedResult)
    }
    //endregion

    //region back button
    /**
     * Back button
     */
    @Test
    fun `given product sheet and variant sheet is shown respectively, when back button is clicked, then variant sheet should be hidden`() {
        playViewModel.onShowProductSheet(123)
        playViewModel.onShowVariantSheet(123, modelBuilder.buildProductLineUiModel(), ProductAction.Buy)
        val isHandled = playViewModel.onBackPressed()

        Assertions
                .assertThat(playViewModel.observableBottomInsetsState.getOrAwaitValue()[BottomInsetsType.VariantSheet]?.isShown)
                .isEqualTo(false)

        Assertions
                .assertThat(playViewModel.observableBottomInsetsState.getOrAwaitValue()[BottomInsetsType.ProductSheet]?.isShown)
                .isEqualTo(true)

        Assertions
                .assertThat(isHandled)
                .isEqualTo(true)
    }

    @Test
    fun `given product sheet is shown, when back button is clicked, then product sheet should be hidden`() {
        playViewModel.onShowProductSheet(123)
        val isHandled = playViewModel.onBackPressed()

        Assertions
                .assertThat(playViewModel.observableBottomInsetsState.getOrAwaitValue()[BottomInsetsType.ProductSheet]?.isShown)
                .isEqualTo(false)

        Assertions
                .assertThat(isHandled)
                .isEqualTo(true)
    }

    @Test
    fun `given keyboard is shown, when back button is clicked, then keyboard should be hidden`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.Live.isLive,
                        type = PlayChannelType.Live.value
                )
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.onKeyboardShown(123)
        val isHandled = playViewModel.onBackPressed()

        Assertions
                .assertThat(playViewModel.observableBottomInsetsState.getOrAwaitValue()[BottomInsetsType.Keyboard]?.isShown)
                .isEqualTo(false)

        Assertions
                .assertThat(isHandled)
                .isEqualTo(true)
    }

    @Test
    fun `given no insets is shown, when back button is clicked, then it should return unhandled`() {
        playViewModel.onKeyboardHidden()
        playViewModel.onHideProductSheet()
        playViewModel.onHideVariantSheet()

        val isHandled = playViewModel.onBackPressed()

        Assertions
                .assertThat(isHandled)
                .isEqualTo(false)
    }
    //endregion

    //region video
    @Test
    fun `when channel is active, then video should be configured`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                isActive = true
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        verify(exactly = 1) {
            mockPlayVideoManager.setRepeatMode(false)
        }
    }

    @Test
    fun `when channel is not active, then video should not be configured`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                isActive = false
        )

        playViewModel.getChannelInfo(mockChannel.channelId)

        verify(exactly = 0) {
            mockPlayVideoManager.setRepeatMode(false)
        }
    }

    @Test
    fun `when should start video, play video manager should be called`() {
        playViewModel.startCurrentVideo()

        verify(exactly = 1) {
            mockPlayVideoManager.resume()
        }
    }

    @Test
    fun `when get video duration, it should return correct duration`() {
        val duration = 2141241L
        every { mockPlayVideoManager.getVideoDuration() } returns duration

        val actualDuration = playViewModel.getVideoDuration()

        Assertions
                .assertThat(actualDuration)
                .isEqualTo(duration)
    }
    //endregion

    //region bottom insets
    @Test
    fun `when hide all insets and keyboard is handled, keyboard should be hidden but same with previous state`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.Live.isLive,
                        type = PlayChannelType.Live.value
                )
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.onKeyboardShown(123)

        playViewModel.hideInsets(true)

        val expectedResult = modelBuilder.buildBottomInsetsState(isShown = false, isPreviousSameState = true)

        Assertions
                .assertThat(playViewModel.observableBottomInsetsState.value?.get(BottomInsetsType.Keyboard))
                .isEqualTo(expectedResult)
    }

    @Test
    fun `when hide all insets and keyboard is not handled, keyboard should be hidden with different previous state`() {
        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel.copy(
                videoStream = mockChannel.videoStream.copy(
                        isLive = PlayChannelType.Live.isLive,
                        type = PlayChannelType.Live.value
                )
        )

        playViewModel.getChannelInfo(mockChannel.channelId)
        playViewModel.onKeyboardShown(123)

        playViewModel.hideInsets(false)

        val expectedResult = modelBuilder.buildBottomInsetsState(isShown = false, isPreviousSameState = false)

        Assertions
                .assertThat(playViewModel.observableBottomInsetsState.value?.get(BottomInsetsType.Keyboard))
                .isEqualTo(expectedResult)
    }
    //endregion

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