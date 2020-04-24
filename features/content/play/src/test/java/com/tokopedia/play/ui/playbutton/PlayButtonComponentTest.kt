package com.tokopedia.play.ui.playbutton

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play_common.state.PlayVideoState
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
 * Created by jegul on 30/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayButtonComponentTest {

    private lateinit var component: PlayButtonComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = PlayButtonComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Given channel is vod")
    inner class GivenChannelVOD {

        private val mockChannelType = PlayChannelType.VOD

        @Test
        fun `when video is paused, then play button should be shown`() = runBlockingTest(testDispatcher) {
            val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                    state = PlayVideoState.Pause
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
            verify { component.uiView.showPlayButton() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when video is ended, then repeat button should be shown`() = runBlockingTest(testDispatcher) {
            val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                    state = PlayVideoState.Ended
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
            verify { component.uiView.showRepeatButton() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when video is playing, then play button should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                    state = PlayVideoState.Playing
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given channel is live")
    inner class GivenChannelLive {

        private val mockChannelType = PlayChannelType.Live

        @Test
        fun `when video is paused, then play button should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                    state = PlayVideoState.Pause
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when video is ended, then play button should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                    state = PlayVideoState.Ended
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when video is playing, then play button should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                    state = PlayVideoState.Playing
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    class PlayButtonComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : PlayButtonComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): PlayButtonView {
            return mockk(relaxed = true)
        }
    }
}