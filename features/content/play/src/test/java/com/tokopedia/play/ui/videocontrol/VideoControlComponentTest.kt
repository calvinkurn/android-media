package com.tokopedia.play.ui.videocontrol

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by jegul on 31/01/20
 */
class VideoControlComponentTest {

    private lateinit var component: VideoControlComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = VideoControlComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when video property changed to Live`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video property changed to VOD`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))

        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video stream is changed to live`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = VideoStreamUiModel(
                uriString = "",
                channelType = PlayChannelType.Live,
                isActive = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video stream is changed to VOD`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = VideoStreamUiModel(
                uriString = "",
                channelType = PlayChannelType.VOD,
                isActive = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream))

        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when should set video`() = runBlockingTest(testDispatcher) {
        val mockExoPlayer = mockk<ExoPlayer>()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetVideo(mockExoPlayer))

        verify { component.uiView.setPlayer(mockExoPlayer) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when channel is freeze`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Freeze("", "", "", "")
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verifyAll {
            component.uiView.hide()
            component.uiView.setPlayer(null)
        }
        confirmVerified(component.uiView)
    }

    class VideoControlComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : VideoControlComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): VideoControlView {
            return mockk(relaxed = true)
        }
    }
}