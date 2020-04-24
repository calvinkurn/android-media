package com.tokopedia.play.ui.loading

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play_common.state.PlayVideoState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Created by jegul on 22/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VideoLoadingComponentTest {

    private lateinit var component: VideoLoadingComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = VideoLoadingComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when video is buffering, then loading should be shown`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                state = PlayVideoState.Buffering
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when video is not buffering, then loading should be hidden`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = modelBuilder.buildVideoPropertyUiModel(
                state = PlayVideoState.Playing
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp, mockStateHelper))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class VideoLoadingComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : VideoLoadingComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): VideoLoadingView {
            return mockk(relaxed = true)
        }
    }
}