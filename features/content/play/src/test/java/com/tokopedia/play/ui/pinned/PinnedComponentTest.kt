package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.PlayRoomEvent
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
class PinnedComponentTest {

    private lateinit var component: PinnedComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = PinnedComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
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
        fun `when there is new pinned message, then pinned message should be set and shown`() = runBlockingTest(testDispatcher) {
            val mockPinnedMessage = modelBuilder.buildPinnedMessageUiModel()

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage, mockStateHelper))
            verifyAll {
                component.uiView.setPinnedMessage(mockPinnedMessage)
                component.uiView.show()
            }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when there is new pinned product, then pinned product should be set and shown`() = runBlockingTest(testDispatcher) {
            val mockPinnedProduct = modelBuilder.buildPinnedProductUiModel()

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedProduct, mockStateHelper))
            verifyAll {
                component.uiView.setPinnedProduct(mockPinnedProduct)
                component.uiView.show()
            }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when no pinned is set, then pinned should be hidden`() = runBlockingTest(testDispatcher) {
            val mockPinned = modelBuilder.buildPinnedRemoveUiModel()

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinned, mockStateHelper))
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
        fun `when there is new pinned message, then pinned message should be set and hidden`() = runBlockingTest(testDispatcher) {
            val mockPinnedMessage = modelBuilder.buildPinnedMessageUiModel()

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedMessage, mockStateHelper))
            verifyAll {
                component.uiView.setPinnedMessage(mockPinnedMessage)
                component.uiView.hide()
            }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when there is new pinned product, then pinned product should be set and hidden`() = runBlockingTest(testDispatcher) {
            val mockPinnedProduct = modelBuilder.buildPinnedProductUiModel()

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinnedProduct, mockStateHelper))
            verifyAll {
                component.uiView.setPinnedProduct(mockPinnedProduct)
                component.uiView.hide()
            }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when no pinned is set, then pinned should be hidden`() = runBlockingTest(testDispatcher) {
            val mockPinned = modelBuilder.buildPinnedRemoveUiModel()

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetPinned(mockPinned, mockStateHelper))
            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given pinned is shown")
    inner class GivenPinnedShown {

        private val mockShouldShownPinned = true

        @Test
        fun `when keyboard is shown, then pinned should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    shouldShowPinned = mockShouldShownPinned,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                    mockBottomInsets,
                    mockBottomInsets.isAnyShown,
                    mockBottomInsets.isAnyHidden,
                    mockStateHelper)
            )

            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then pinned should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    shouldShowPinned = mockShouldShownPinned,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                    mockBottomInsets,
                    mockBottomInsets.isAnyShown,
                    mockBottomInsets.isAnyHidden,
                    mockStateHelper)
            )

            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Given pinned is hidden")
    inner class GivenPinnedHidden {

        private val mockShouldShownPinned = false

        @Test
        fun `when keyboard is shown, then pinned should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    shouldShowPinned = mockShouldShownPinned,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                    mockBottomInsets,
                    mockBottomInsets.isAnyShown,
                    mockBottomInsets.isAnyHidden,
                    mockStateHelper)
            )

            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then pinned should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    shouldShowPinned = mockShouldShownPinned,
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                    mockBottomInsets,
                    mockBottomInsets.isAnyShown,
                    mockBottomInsets.isAnyHidden,
                    mockStateHelper)
            )

            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }
    }

    @Test
    fun `when channel is frozen, then pinned should be hidden`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomFreezeEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then pinned should be hidden`() = runBlockingTest(testDispatcher) {
        val mockBanned = modelBuilder.buildPlayRoomBannedEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockBanned))
        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    class PinnedComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : PinnedComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): PinnedView {
            return mockk(relaxed = true)
        }
    }
}