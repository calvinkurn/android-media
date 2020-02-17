package com.tokopedia.play.ui.onetap

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
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
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by jegul on 30/01/20
 */
class OneTapComponentTest {

    private lateinit var component: OneTapComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = OneTapComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when show one tap`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ShowOneTapOnboarding)

        verify { component.uiView.showAnimated() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when channel is freeze`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Freeze("", "", "", "")
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class OneTapComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : OneTapComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): OneTapView {
            return mockk(relaxed = true)
        }
    }
}