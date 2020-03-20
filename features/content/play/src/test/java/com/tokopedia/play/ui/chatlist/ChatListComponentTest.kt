package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.PlayChatUiModel
import com.tokopedia.play.view.uimodel.VideoPropertyUiModel
import com.tokopedia.play.view.uimodel.VideoStreamUiModel
import com.tokopedia.play_common.state.PlayVideoState
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

    private val modelBuilder = ModelBuilder()

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
    fun `given keyboard is hidden, when channel is changed to live`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                channelType = PlayChannelType.Live
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                channelType = PlayChannelType.Live
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given keyboard is hidden, when channel is changed to vod`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                channelType = PlayChannelType.VOD
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                channelType = PlayChannelType.VOD
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given keyboard is shown, when channel is changed to live`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                channelType = PlayChannelType.Live
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                channelType = PlayChannelType.Live,
                bottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
                )
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `given keyboard is shown, when channel is changed to vod`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                channelType = PlayChannelType.VOD
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                channelType = PlayChannelType.VOD,
                bottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
                )
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when there is new incoming chat`() = runBlockingTest(testDispatcher) {
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

    class ChatListComponentMock(container: ViewGroup, bus: EventBusFactory, coroutineScope: CoroutineScope) : ChatListComponent(container, bus, coroutineScope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): ChatListView {
            return mockk(relaxed = true)
        }
    }
}