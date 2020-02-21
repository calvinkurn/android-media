package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.play.domain.GetChannelInfoUseCase
import com.tokopedia.play.domain.GetIsLikeUseCase
import com.tokopedia.play.domain.GetPartnerInfoUseCase
import com.tokopedia.play.domain.GetTotalLikeUseCase
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.helper.getOrAwaitValue
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.ChannelInfoUiModel
import com.tokopedia.play.view.uimodel.TotalLikeUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play_common.player.TokopediaPlayManager
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

    private val mockPlayManager: TokopediaPlayManager = mockk(relaxed = true)
    private val mockGetChannelInfoUseCase: GetChannelInfoUseCase = mockk(relaxed = true)
    private val mockGetPartnerInfoUseCase: GetPartnerInfoUseCase = mockk(relaxed = true)
    private val mockGetTotalLikeUseCase: GetTotalLikeUseCase = mockk(relaxed = true)
    private val mockGetIsLikeUseCase: GetIsLikeUseCase = mockk(relaxed = true)
    private val mockPlaySocket: PlaySocket = mockk()
    private val userSession: UserSessionInterface = mockk(relaxed = true)
    private val dispatchers: CoroutineDispatcherProvider = TestCoroutineDispatchersProvider

    private val modelBuilder = ModelBuilder()

    private val mockChannel = modelBuilder.buildChannel()
    private val mockShopInfo = modelBuilder.buildShopInfo()
    private val mockNewChat = modelBuilder.buildNewChat()

    private val mockTotalLikeContentData = modelBuilder.buildTotalLike()
    private val mockTotalLike = TotalLike(mockTotalLikeContentData.like.value, mockTotalLikeContentData.like.fmt)

    private val mockIsLikeContentData = modelBuilder.buildIsLike()
    private val mockIsLike = mockIsLikeContentData.isLike

    private lateinit var playViewModel: PlayViewModel

    @Before
    fun setUp() {
        playViewModel = PlayViewModel(
                mockPlayManager,
                mockGetChannelInfoUseCase,
                mockGetPartnerInfoUseCase,
                mockGetTotalLikeUseCase,
                mockGetIsLikeUseCase,
                mockPlaySocket,
                userSession,
                dispatchers
        )

        coEvery { mockGetChannelInfoUseCase.executeOnBackground() } returns mockChannel
        coEvery { mockGetPartnerInfoUseCase.executeOnBackground() } returns mockShopInfo
        coEvery { mockGetTotalLikeUseCase.executeOnBackground() } returns mockTotalLike
        coEvery { mockGetIsLikeUseCase.executeOnBackground() } returns mockIsLike
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

        every { mockPlayManager.getObservableVideoPlayer() } returns mockObservableVideoPlayer

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
                likeType = mockChannel.likeType
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
}