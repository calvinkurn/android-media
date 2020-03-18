package com.tokopedia.play.ui.endliveinfo

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
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
 * Created by jegul on 30/01/20
 */
class EndLiveInfoComponentTest {

    private lateinit var component: EndLiveInfoComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = EndLiveInfoComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when channel is freeze`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Freeze(
                "Channel telah berakhir",
                "Silahkan pilih channel lain",
                "Keluar",
                ""
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))
        verifyAll {
            component.uiView.show()
            component.uiView.setInfo(
                    mockPlayRoomEvent.title,
                    mockPlayRoomEvent.message,
                    mockPlayRoomEvent.btnTitle,
                    mockPlayRoomEvent.btnUrl
            )
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when user is banned`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Banned(
                "Channel telah berakhir",
                "Silahkan pilih channel lain",
                "Keluar"
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))
        verify { component.uiView.hide() }
        verify(exactly = 0) {
            component.uiView.setInfo(
                    mockPlayRoomEvent.title,
                    mockPlayRoomEvent.message,
                    mockPlayRoomEvent.btnTitle,
                    any()
            )
        }
        confirmVerified(component.uiView)
    }

    class EndLiveInfoComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : EndLiveInfoComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): EndLiveInfoView {
            return mockk(relaxed = true)
        }
    }
}