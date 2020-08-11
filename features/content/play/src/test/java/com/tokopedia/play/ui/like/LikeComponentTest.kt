package com.tokopedia.play.ui.like

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
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
import org.junit.jupiter.api.*

/**
 * Created by jegul on 30/01/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LikeComponentTest {

    private lateinit var component: LikeComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = LikeComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Keyboard state is changing")
    inner class KeyboardStateChanged{

        @Test
        fun `when keyboard is shown, then like should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))

            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when keyboard is hidden, then like should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    keyboardState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))

            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Product Sheet state is changing")
    inner class ProductSheetStateChanged{

        @Test
        fun `when product sheet is shown, then like should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    productSheetState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))

            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when product sheet is hidden, then like should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    productSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))

            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }
    }

    @Nested
    @DisplayName("Variant Sheet state is changing")
    inner class VariantSheetStateChanged{

        @Test
        fun `when variant sheet is shown, then like should be hidden`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    variantSheetState = modelBuilder.buildBottomInsetsState(isShown = true)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))

            verify { component.uiView.hide() }
            confirmVerified(component.uiView)
        }

        @Test
        fun `when variant sheet is hidden, then like should be shown`() = runBlockingTest(testDispatcher) {
            val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                    variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
            )

            val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                    bottomInsets = mockBottomInsets
            )

            EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(mockBottomInsets, mockBottomInsets.isAnyShown, mockBottomInsets.isAnyHidden, mockStateHelper))

            verify { component.uiView.show() }
            confirmVerified(component.uiView)
        }
    }

    @Test
    fun `when channel is frozen, then like should be hidden`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = modelBuilder.buildPlayRoomFreezeEvent()
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then like should be hidden`() = runBlockingTest(testDispatcher) {
        val mockPlayRoomEvent = modelBuilder.buildPlayRoomBannedEvent()
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockPlayRoomEvent))

        verify { component.uiView.hide() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when new total likes is retrieved, then like count should be updated `() = runBlockingTest(testDispatcher) {
        val mockTotalLike = modelBuilder.buildTotalLikeUiModel()
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetTotalLikes(mockTotalLike))

        verify { component.uiView.setTotalLikes(mockTotalLike) }
        confirmVerified(component.uiView)
    }

    class LikeComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : LikeComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): LikeView {
            return mockk(relaxed = true)
        }
    }
}