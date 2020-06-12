package com.tokopedia.play.ui.onetap

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.VideoOrientation
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Created by jegul on 30/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OneTapComponentTest {

    private lateinit var component: OneTapComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = OneTapComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given orientation portrait and video vertical, when one tap is called, then one tap should shown`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ShowOneTapOnboarding(
                modelBuilder.buildStateHelperUiModel(
                        videoOrientation = VideoOrientation.Vertical,
                        screenOrientation = ScreenOrientation.Portrait
                )
        ))

        verify { component.uiView.showAnimated() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given orientation portrait and video horizontal, when one tap is called, then one tap should shown`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ShowOneTapOnboarding(
                modelBuilder.buildStateHelperUiModel(
                        videoOrientation = VideoOrientation.Horizontal(16, 9),
                        screenOrientation = ScreenOrientation.Portrait
                )
        ))

        verify(exactly = 0) { component.uiView.showAnimated() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given orientation landscape, when one tap is called, then one tap should shown`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ShowOneTapOnboarding(
                modelBuilder.buildStateHelperUiModel(
                        screenOrientation = ScreenOrientation.Landscape
                )
        ))

        verify(exactly = 0) { component.uiView.showAnimated() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when channel is frozen, then one tap should be hidden`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = modelBuilder.buildPlayRoomFreezeEvent()
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then one tap should be hidden`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = modelBuilder.buildPlayRoomBannedEvent()
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class OneTapComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : OneTapComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): OneTapView {
            return mockk(relaxed = true)
        }
    }
}