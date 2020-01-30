package com.tokopedia.play.ui.like

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.view.event.ScreenStateEvent
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
class LikeComponentTest {

    private lateinit var component: LikeComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = LikeComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when keyboard is shown`() = runBlockingTest(testDispatcher) {
        val isKeyboardShown = true
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(isKeyboardShown))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when keyboard is hidden`() = runBlockingTest(testDispatcher) {
        val isKeyboardShown = false
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(isKeyboardShown))

        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when like content`() = runBlockingTest(testDispatcher) {
        val shouldLike = true
        val animate = false
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.LikeContent(shouldLike, animate))

        verify { component.uiView.playLikeAnimation(shouldLike, animate) }
        confirmVerified(component.uiView)
    }

    class LikeComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : LikeComponent(container, bus, coroutineScope) {
        override fun initView(container: ViewGroup): LikeView {
            return mockk(relaxed = true)
        }
    }
}