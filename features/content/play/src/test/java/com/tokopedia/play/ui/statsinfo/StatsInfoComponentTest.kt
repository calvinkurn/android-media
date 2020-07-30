package com.tokopedia.play.ui.statsinfo

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
 * Created by jegul on 24/03/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatsInfoComponentTest {

    private lateinit var component: StatsInfoComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = StatsInfoComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when channel type is changing to VOD, then live badge should be hidden`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                channelType = PlayChannelType.VOD
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                channelType = mockVideoStream.channelType
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
        verify { component.uiView.setLiveBadgeVisibility(false) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when channel type is changing to Live, then live badge should be shown`() = runBlockingTest(testDispatcher) {
        val mockVideoStream = modelBuilder.buildVideoStreamUiModel(
                channelType = PlayChannelType.Live
        )

        val mockStateHelper = modelBuilder.buildStateHelperUiModel(
                channelType = mockVideoStream.channelType
        )

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(mockVideoStream, mockStateHelper))
        verify { component.uiView.setLiveBadgeVisibility(true) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when new total view is retrieved, then total view should be updated`() = runBlockingTest(testDispatcher) {
        val mockTotalView = modelBuilder.buildTotalViewUiModel()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.SetTotalViews(mockTotalView))
        verify { component.uiView.setTotalViews(mockTotalView) }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when channel is frozen, then stats info should be shown`() = runBlockingTest(testDispatcher) {
        val mockFreeze = modelBuilder.buildPlayRoomFreezeEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockFreeze))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Test
    fun `when user is banned, then stats info should be shown`() = runBlockingTest(testDispatcher) {
        val mockBanned = modelBuilder.buildPlayRoomBannedEvent()

        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNewPlayRoomEvent(mockBanned))
        verify { component.uiView.show() }
        confirmVerified(component.uiView)
    }

    @Nested
    @DisplayName("Keyboard state is changing")
    inner class KeyboardStateChanged{

        @Test
        fun `when keyboard is shown, then stats info should be hidden`() = runBlockingTest(testDispatcher) {
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
        fun `when keyboard is hidden, then stats info should be shown`() = runBlockingTest(testDispatcher) {
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
        fun `when product sheet is shown, then stats info should be hidden`() = runBlockingTest(testDispatcher) {
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
        fun `when product sheet is hidden, then stats info should be shown`() = runBlockingTest(testDispatcher) {
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
        fun `when variant sheet is shown, then stats info should be hidden`() = runBlockingTest(testDispatcher) {
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
        fun `when variant sheet is hidden, then stats info should be shown`() = runBlockingTest(testDispatcher) {
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

    class StatsInfoComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : StatsInfoComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): StatsInfoView {
            return mockk(relaxed = true)
        }
    }
}