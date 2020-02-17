package com.tokopedia.play.ui.playbutton

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play_common.state.TokopediaPlayVideoState
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
class PlayButtonComponentTest {

    private lateinit var component: PlayButtonComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = PlayButtonComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when VOD is playing`() = runBlockingTest(testDispatcher) {
        val mockVideoProperty = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProperty))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when VOD is pause`() = runBlockingTest(testDispatcher) {
        val mockVideoProperty = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Pause
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProperty))
        verify { component.uiView.showPlayButton() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when Live is playing`() = runBlockingTest(testDispatcher) {
        val mockVideoProperty = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProperty))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when Live is pause`() = runBlockingTest(testDispatcher) {
        val mockVideoProperty = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Pause
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProperty))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when VOD is ended`() = runBlockingTest(testDispatcher) {
        val mockVideoProperty = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Ended
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProperty))
        verify { component.uiView.showRepeatButton() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when Live is ended`() = runBlockingTest(testDispatcher) {
        val mockVideoProperty = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Ended
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProperty))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class PlayButtonComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : PlayButtonComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): PlayButtonView {
            return mockk(relaxed = true)
        }
    }
}