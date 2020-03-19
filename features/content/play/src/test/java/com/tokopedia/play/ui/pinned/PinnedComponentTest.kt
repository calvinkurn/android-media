package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.PinnedMessageUiModel
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
class PinnedComponentTest {

    private lateinit var component: PinnedComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = PinnedComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test remove pinned message`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test set pinned message`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = false
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))
        verifyAll {
            component.uiView.setPinnedMessage(mockPinnedMessage)
            component.uiView.show()
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test set pinned when keyboard is hidden`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = false
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(false))
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))

        verifyOrder {
            component.uiView.hide()
            component.uiView.setPinnedMessage(mockPinnedMessage)
            component.uiView.show()
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test set pinned when keyboard is shown`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = false
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(true))
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))

        verifyOrder {
            component.uiView.hide()
            component.uiView.setPinnedMessage(mockPinnedMessage)
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test show keyboard when pinned is set`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = false
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(true))

        verifyOrder {
            component.uiView.setPinnedMessage(mockPinnedMessage)
            component.uiView.show()
            component.uiView.hide()
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test hide keyboard when pinned is set`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = false
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(false))

        verifyOrder {
            component.uiView.setPinnedMessage(mockPinnedMessage)
            component.uiView.show()
            component.uiView.show()
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test show keyboard when pinned is not set`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(true))

        verifyOrder {
            component.uiView.hide()
            component.uiView.hide()
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test hide keyboard when pinned is not set`() = runBlockingTest(testDispatcher) {
        val mockPinnedMessage = PinnedMessageUiModel(
                applink = null,
                partnerName = "My Shop",
                title = "Dibeli dibeli",
                shouldRemove = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage))
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(false))

        verifyOrder {
            component.uiView.hide()
            component.uiView.hide()
        }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when channel is freeze`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Freeze("", "", "", "")
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class PinnedComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : PinnedComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): PinnedView {
            return mockk(relaxed = true)
        }
    }
}