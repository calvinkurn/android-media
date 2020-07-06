package com.tokopedia.play.ui.video

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.util.video.PlayVideoUtil
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Created by jegul on 31/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VideoComponentTest {

    private lateinit var component: VideoComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val mockPlayVideoUtil = mockk<PlayVideoUtil>(relaxed = true)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = VideoComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope, mockPlayVideoUtil)
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

    class VideoComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope, playVideoUtil: PlayVideoUtil) : VideoComponent(container, bus, scope, TestCoroutineDispatchersProvider, playVideoUtil) {
        override fun initView(container: ViewGroup): VideoView {
            return mockk(relaxed = true)
        }
    }
}