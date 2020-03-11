package com.tokopedia.play.ui.quickreply

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.QuickReplyUiModel
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
class QuickReplyComponentTest {

    private lateinit var component: QuickReplyComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = QuickReplyComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test set quick reply`() = runBlockingTest(testDispatcher) {
        val mockQuickReply = QuickReplyUiModel(
                quickReplyList = listOf("Keren", "Wih, mantap jiwa")
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetQuickReply(mockQuickReply))
        verify { component.uiView.setQuickReply(mockQuickReply) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when keyboard is shown`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(true))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when keyboard is hidden`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(false))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when freeze`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Freeze("", "", "", "")

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class QuickReplyComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : QuickReplyComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): QuickReplyView {
            return mockk(relaxed = true)
        }
    }
}