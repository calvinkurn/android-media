package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayChatUiModel
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
 * Created by jegul on 30/01/20
 */
class ChatListComponentTest {

    private lateinit var component: ChatListComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = ChatListComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test when new chat incoming`() = runBlockingTest(testDispatcher) {
        val mockChat = PlayChatUiModel(
                messageId = "1",
                userId = "1251",
                name = "Lukas",
                message = "Keren banget fitur ini.",
                isSelfMessage = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.IncomingChat(mockChat))
        verify { component.uiView.showChat(mockChat) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video property is live`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.Live,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video property is VOD`() = runBlockingTest(testDispatcher) {
        val mockVideoProp = VideoPropertyUiModel(
                type = PlayChannelType.VOD,
                state = TokopediaPlayVideoState.Playing
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoPropertyChanged(mockVideoProp))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `test when video stream is live`() = runBlockingTest(testDispatcher) {
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
    fun `test when video stream is VOD`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = VideoStreamUiModel(
                uriString = "",
                channelType = PlayChannelType.VOD,
                isActive = true
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class ChatListComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : ChatListComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): ChatListView {
            return mockk(relaxed = true)
        }
    }
}