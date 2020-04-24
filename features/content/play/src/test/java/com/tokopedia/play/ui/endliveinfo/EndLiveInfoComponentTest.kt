package com.tokopedia.play.ui.endliveinfo

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import io.mockk.*
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
class EndLiveInfoComponentTest {

    private lateinit var component: EndLiveInfoComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = EndLiveInfoComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when there is new total view, then info should show it`() = runBlockingTest(testDispatcher) {
        val mockTotalView = modelBuilder.buildTotalViewUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetTotalViews(mockTotalView))
        verify { component.uiView.setTotalViews(mockTotalView) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when there is new total like, then info should show it`() = runBlockingTest(testDispatcher) {
        val mockTotalLike = modelBuilder.buildTotalLikeUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetTotalLikes(mockTotalLike))
        verify { component.uiView.setTotalLikes(mockTotalLike) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when channel is frozen, then info should be shown`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = modelBuilder.buildPlayRoomFreezeEvent()

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
    fun `when user is banned, then info should be shown`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = modelBuilder.buildPlayRoomBannedEvent()

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

    class EndLiveInfoComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : EndLiveInfoComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): EndLiveInfoView {
            return mockk(relaxed = true)
        }
    }
}