package com.tokopedia.play.ui.loading

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by jegul on 22/01/20
 */
class VideoLoadingComponentTest {

    private lateinit var component: VideoLoadingComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = VideoLoadingComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testLoadingVODBuffer() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Buffering
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verifyOrder {
            component.uiView.hide()
            component.uiView.show()
        }
    }

    @Test
    fun testLoadingLiveBuffer() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Buffering
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verifyOrder {
            component.uiView.hide()
            component.uiView.show()
        }
    }

    @Test
    fun testPlayingVOD() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verify { component.uiView.hide() }
        verify(exactly = 0) { component.uiView.show() }
    }

    @Test
    fun testPlayingLive() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verify { component.uiView.hide() }
        verify(exactly = 0) { component.uiView.show() }
    }

    class VideoLoadingComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : VideoLoadingComponent(container, bus, coroutineScope) {
        override fun initView(container: ViewGroup): VideoLoadingView {
            return mockk(relaxed = true)
        }
    }
}