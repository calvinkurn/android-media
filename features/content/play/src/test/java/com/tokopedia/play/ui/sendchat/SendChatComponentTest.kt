package com.tokopedia.play.ui.sendchat

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
import io.mockk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*

/**
 * Created by jegul on 31/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SendChatComponentTest {

    private lateinit var component: SendChatComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = SendChatComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when user wants to compose chat, then chat should be focused on`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ComposeChat)
        verify { component.uiView.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true) }
        confirmVerified(component.uiView)
    }

    @Nested
    @DisplayName("Given channel is VOD")
    inner class GivenChannelVOD {

        private val mockChannelType = PlayChannelType.VOD

        @Test
        fun `when keyboard is shown, then send chat should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verifyAll {
                component.uiView.hide()
                component.uiView.focusChatForm(false)
            }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then send chat should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verifyAll {
                component.uiView.hide()
                component.uiView.focusChatForm(false)
            }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given channel is live")
    inner class GivenChannelLive {

        private val mockChannelType = PlayChannelType.Live

        @Test
        fun `when keyboard is shown, then send chat should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verifyAll {
                component.uiView.show()
                component.uiView.focusChatForm(true)
            }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then send chat should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verifyAll {
                component.uiView.show()
                component.uiView.focusChatForm(false)
            }
            confirmVerified(component.uiView)
        }
    }

    @Test
    fun `when channel is frozen, then send chat should be hidden and focus removed`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomFreezeEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verifySequence {
            component.uiView.focusChatForm(false)
            component.uiView.hide()
        }
    }

    @Test
    fun `when user is banned, then send chat should be hidden and focus removed`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomBannedEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verifySequence {
            component.uiView.focusChatForm(false)
            component.uiView.hide()
        }
    }

    class SendChatComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : SendChatComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): SendChatView {
            return mockk(relaxed = true)
        }
    }
}