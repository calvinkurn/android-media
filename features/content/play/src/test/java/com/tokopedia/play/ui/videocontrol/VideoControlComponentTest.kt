package com.tokopedia.play.ui.videocontrol

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.General
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*

/**
 * Created by jegul on 31/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VideoControlComponentTest {

    private lateinit var component: VideoControlComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val mockGeneralVideoPlayer = General(mockk())

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = VideoControlComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when new video is set, then video should update video`() = runBlockingTest(testDispatcher) {
        val mockGeneralVideoPlayer = modelBuilder.buildGeneralVideoUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetVideo(mockGeneralVideoPlayer))

        verify { component.uiView.setPlayer(mockGeneralVideoPlayer.exoPlayer) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when channel is frozen, then video should be hidden and released`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomFreezeEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify {
            component.uiView.hide()
            component.uiView.setPlayer(null)
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then video should be hidden and released`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomBannedEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify {
            component.uiView.hide()
            component.uiView.setPlayer(null)
        }
        confirmVerified(component.uiView)
    }

    @Nested
    @DisplayName("Given channel is VOD")
    inner class GivenChannelVOD {

        val mockChannelType = PlayChannelType.VOD

        @Test
        fun `when keyboard is shown, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given channel is Live")
    inner class GivenChannelLive {

        val mockChannelType = PlayChannelType.Live

        @Test
        fun `when keyboard is shown, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given keyboard is shown")
    inner class GivenKeyboardShown {

        val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
        )

        @Test
        fun `when channel is changed to vod, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.VOD
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when channel is changed to live, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.Live
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given keyboard is hidden")
    inner class GivenKeyboardHidden {

        val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
        )

        @Test
        fun `when channel is changed to vod, then video control should be shown`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.VOD
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when channel is changed to live, then video control should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.Live
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets,
                    videoPlayer = mockGeneralVideoPlayer
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    class VideoControlComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : VideoControlComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): VideoControlView {
            return mockk(relaxed = true)
        }
    }
}