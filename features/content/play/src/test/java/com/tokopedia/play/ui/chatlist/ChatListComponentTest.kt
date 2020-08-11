package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
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
import org.junit.jupiter.api.*

/**
 * Created by jegul on 30/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatListComponentTest {

    private lateinit var component: ChatListComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = ChatListComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Given keyboard is hidden")
    inner class GivenKeyboardHidden {

        private val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
        )

        @Test
        fun `when channel is changed to live, then chat list should be shown`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.Live
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when channel is changed to vod, then chat list should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.VOD
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given keyboard is shown")
    inner class GivenKeyboardShown {

        private val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
        )

        @Test
        fun `when channel is changed to live, then chat list should be shown`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.Live
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when channel is changed to vod, then chat list should be hidden`() = runBlockingTest(testDispatcher) {
            val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                    channelType = PlayChannelType.VOD
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockVideoStream.channelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given channel is live")
    inner class GivenLiveChannel {

        private val mockChannelType = PlayChannelType.Live

        @Test
        fun `when keyboard is shown, then chat list should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.BottomInsetsChanged(
                            mockBottomInsets,
                            mockBottomInsets.isAnyShown,
                            mockBottomInsets.isAnyHidden,
                            mockStateHelper
                    )
            )
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then chat list should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.BottomInsetsChanged(
                            mockBottomInsets,
                            mockBottomInsets.isAnyShown,
                            mockBottomInsets.isAnyHidden,
                            mockStateHelper
                    )
            )
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given channel is vod")
    inner class GivenVODChannel {

        private val mockChannelType = PlayChannelType.VOD

        @Test
        fun `when keyboard is shown, then chat list should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.BottomInsetsChanged(
                            mockBottomInsets,
                            mockBottomInsets.isAnyShown,
                            mockBottomInsets.isAnyHidden,
                            mockStateHelper
                    )
            )
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then chat list should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    channelType = mockChannelType,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.BottomInsetsChanged(
                            mockBottomInsets,
                            mockBottomInsets.isAnyShown,
                            mockBottomInsets.isAnyHidden,
                            mockStateHelper
                    )
            )
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Test
    fun `when there is new incoming chat, then chat list should show it`() = runBlockingTest(testDispatcher) {
        val mockChat = modelBuilder.buildPlayChatUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.IncomingChat(mockChat))
        verify { component.uiView.showNewChat(mockChat) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when channel is freeze, then chat list should be hidden`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomFreezeEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then chat list should be hidden`() = runBlockingTest(testDispatcher) {
        val mockBanned = modelBuilder.buildPlayRoomBannedEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockBanned))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class ChatListComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : ChatListComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): ChatListView {
            return mockk(relaxed = true)
        }
    }
}