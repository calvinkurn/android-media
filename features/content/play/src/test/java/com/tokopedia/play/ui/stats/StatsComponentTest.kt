package com.tokopedia.play.ui.stats

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.TotalLikeUiModel
import com.tokopedia.play.view.uimodel.TotalViewUiModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Created by jegul on 27/01/20
 */
class StatsComponentTest {

    private lateinit var component: StatsComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = StatsComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test set total views`() = runBlockingTest(testDispatcher) {
        val mockTotalView = TotalViewUiModel(
                totalView = "1.5k"
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetTotalViews(mockTotalView))
        verify { component.uiView.setTotalViews(mockTotalView) }
        verify(exactly = 0) { component.uiView.setTotalLikes(any()) }
    }

    @Test
    fun `test set total likes`() = runBlockingTest(testDispatcher) {
        val mockTotalLike = TotalLikeUiModel(
                totalLike = 1300,
                totalLikeFormatted = "1.3k likes"
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetTotalLikes(mockTotalLike))
        verify { component.uiView.setTotalLikes(mockTotalLike) }
        verify(exactly = 0) { component.uiView.setTotalViews(any()) }
    }

    @Test
    fun `test when channel freeze`() = runBlockingTest(testDispatcher) {
        val mockEvent = PlayRoomEvent.Freeze("", "", "", "")

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockEvent))
        verify(exactly = 1) { component.uiView.hide() }
        verify(exactly = 0) { component.uiView.show() }
    }

    @Test
    fun `test when user is banned`() = runBlockingTest(testDispatcher) {
        val mockEvent = PlayRoomEvent.Banned("", "", "")

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockEvent))
        verify(exactly = 0) { component.uiView.hide() }
        verify(exactly = 0) { component.uiView.show() }
    }

    @Test
    fun `test when keyboard is hidden`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(false))
        verify(exactly = 0) { component.uiView.hide() }
        verify(exactly = 1) { component.uiView.show() }
    }

    @Test
    fun `test when keyboard is shown`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(true))
        verify(exactly = 1) { component.uiView.hide() }
        verify(exactly = 0) { component.uiView.show() }
    }

    class StatsComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : StatsComponent(container, bus, coroutineScope) {
        override fun initView(container: ViewGroup): StatsView {
            return mockk(relaxed = true)
        }
    }
}