package com.tokopedia.play.ui.sendchat

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
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
 * Created by jegul on 31/01/20
 */
class SendChatComponentTest {

    private lateinit var component: SendChatComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = SendChatComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when video property changed to Live`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))

        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video property changed to VOD`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video stream is changed to live`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = VideoStreamUiModel(
                uriString = "",
                channelType = PlayChannelType.Live,
                isActive = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream))

        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video stream is changed to VOD`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = VideoStreamUiModel(
                uriString = "",
                channelType = PlayChannelType.VOD,
                isActive = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when should compose chat`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ComposeChat)

        verify { component.uiView.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when keyboard state is changed`() = runBlockingTest(testDispatcher) {
        val isKeyboardShown = true
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(isKeyboardShown))

        verify { component.uiView.focusChatForm(isKeyboardShown) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when channel is freeze`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = PlayRoomEvent.Freeze("", "", "", "")
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class SendChatComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : SendChatComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): SendChatView {
            return mockk(relaxed = true)
        }
    }
}