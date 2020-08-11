package com.tokopedia.play.ui.quickreply

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayChannelType
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
import org.junit.jupiter.api.*

/**
 * Created by jegul on 30/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QuickReplyComponentTest {

    private lateinit var component: QuickReplyComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = QuickReplyComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when there are new quick replies, then new quick replies should be set`() = runBlockingTest(testDispatcher) {
        val mockQuickReply = modelBuilder.buildQuickReplyUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetQuickReply(mockQuickReply))
        verify { component.uiView.setQuickReply(mockQuickReply) }
    }

    @Nested
    @DisplayName("Given channel is VOD")
    inner class GivenChannelVOD {

        private val mockChannelType = PlayChannelType.VOD

        @Test
        fun `when keyboard is shown, then quick reply should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then quick reply should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given channel is Live")
    inner class GivenChannelLive {

        private val mockChannelType = PlayChannelType.Live

        @Test
        fun `when keyboard is shown, then quick reply should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then quick reply should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets,
                    channelType = mockChannelType
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Test
    fun `when channel is frozen, then quick reply should be hidden`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomFreezeEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then quick reply should be hidden`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomBannedEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class QuickReplyComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : QuickReplyComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): QuickReplyView {
            return mockk(relaxed = true)
        }
    }
}