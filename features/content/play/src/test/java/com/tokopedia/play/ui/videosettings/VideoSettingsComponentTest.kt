package com.tokopedia.play.ui.videosettings

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.helper.TestCoroutineDispatchersProvider
import com.tokopedia.play.model.ModelBuilder
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.General
import com.tokopedia.play.view.uimodel.YouTube
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.*

/**
 * Created by jegul on 12/05/20
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VideoSettingsComponentTest {

    private lateinit var component: VideoSettingsComponent
    private val owner = mockk<LifecycleOwner>(relaxed = true)

    private val testDispatcher = TestCoroutineDispatcher()
    private val coroutineScope = CoroutineScope(testDispatcher)

    private val modelBuilder = ModelBuilder()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { owner.lifecycle } returns mockk(relaxed = true)

        component = VideoSettingsComponentMock(mockk(relaxed = true), EventBusFactory.get(owner), coroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("Given all bottom insets are hidden")
    inner class GivenAllBottomInsetsHiddenTest {

        private val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                keyboardState = modelBuilder.buildBottomInsetsState(isShown = false),
                productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
        )

        private var mockStateHelper = modelBuilder.buildStateHelperUiModel(
                bottomInsets = mockBottomInsets
        )

        @Nested
        @DisplayName("When new video is general type")
        inner class WhenVideoGeneralTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoPlayer = General(mockk())
                )
            }

            @Test
            fun `when video orientation is vertical, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Vertical),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when video orientation is horizontal, then video settings should be shown`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Horizontal(20, 10)),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.show() }
            }
        }

        @Nested
        @DisplayName("When new video is youtube type")
        inner class WhenVideoYouTubeTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoPlayer = YouTube("abrbeb")
                )
            }

            @Test
            fun `when video orientation is vertical, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Vertical),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when video orientation is horizontal, then video settings should be shown`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Horizontal(20, 10)),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }
    }

    @Nested
    @DisplayName("Given any bottom insets are shown")
    inner class GivenAnyBottomInsetsShownTest {

        private val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                keyboardState = modelBuilder.buildBottomInsetsState(isShown = true),
                productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
        )

        private var mockStateHelper = modelBuilder.buildStateHelperUiModel(
                bottomInsets = mockBottomInsets
        )

        @Nested
        @DisplayName("When new video is general type")
        inner class WhenVideoGeneralTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoPlayer = General(mockk())
                )
            }

            @Test
            fun `when video orientation is vertical, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Vertical),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when video orientation is horizontal, then video settings should be shown`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Horizontal(20, 10)),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }

        @Nested
        @DisplayName("When new video is youtube type")
        inner class WhenVideoYouTubeTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoPlayer = YouTube("abrbeb")
                )
            }

            @Test
            fun `when video orientation is vertical, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Vertical),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when video orientation is horizontal, then video settings should be shown`() = runBlockingTest(testDispatcher) {
                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.VideoStreamChanged(
                        videoStream = modelBuilder.buildVideoStreamUiModel(orientation = VideoOrientation.Horizontal(20, 10)),
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }
    }

    @Nested
    @DisplayName("Given video is general type")
    inner class GivenVideoIsGeneralTest {

        private var mockStateHelper = modelBuilder.buildStateHelperUiModel(
                videoPlayer = General(mockk())
        )

        @Nested
        @DisplayName("When video is in horizontal orientation")
        inner class WhenVideoIsHorizontalTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoOrientation = VideoOrientation.Horizontal(10, 20)
                )
            }

            @Test
            fun `when all bottom insets are hidden, then video settings should be shown`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = false),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.show() }
            }

            @Test
            fun `when any bottom insets is shown, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = true),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }

        @Nested
        @DisplayName("When video is in vertical orientation")
        inner class WhenVideoIsVerticalTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoOrientation = VideoOrientation.Vertical
                )
            }

            @Test
            fun `when all bottom insets are hidden, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = false),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when any bottom insets is shown, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = true),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }
    }

    @Nested
    @DisplayName("Given video is youtube type")
    inner class GivenVideoIsYouTubeTest {

        private var mockStateHelper = modelBuilder.buildStateHelperUiModel(
                videoPlayer = YouTube("arbe")
        )

        @Nested
        @DisplayName("When video is in horizontal orientation")
        inner class WhenVideoIsHorizontalTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoOrientation = VideoOrientation.Horizontal(10, 20)
                )
            }

            @Test
            fun `when all bottom insets are hidden, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = false),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when any bottom insets is shown, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = true),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }

        @Nested
        @DisplayName("When video is in vertical orientation")
        inner class WhenVideoIsVerticalTest {

            init {
                mockStateHelper = mockStateHelper.copy(
                        videoOrientation = VideoOrientation.Vertical
                )
            }

            @Test
            fun `when all bottom insets are hidden, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = false),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }

            @Test
            fun `when any bottom insets is shown, then video settings should be hidden`() = runBlockingTest(testDispatcher) {
                val mockBottomInsets = modelBuilder.buildBottomInsetsMap(
                        keyboardState = modelBuilder.buildBottomInsetsState(isShown = true),
                        productSheetState = modelBuilder.buildBottomInsetsState(isShown = false),
                        variantSheetState = modelBuilder.buildBottomInsetsState(isShown = false)
                )

                EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.BottomInsetsChanged(
                        insetsViewMap = mockBottomInsets,
                        isAnyShown = mockBottomInsets.isAnyShown,
                        isAnyHidden = mockBottomInsets.isAnyHidden,
                        stateHelper = mockStateHelper
                ))

                verifySequence { component.uiView.hide() }
            }
        }
    }

    @Test
    fun `when should immersive, then video settings should fade out`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ImmersiveStateChanged(true))

        verifySequence { component.uiView.fadeOut() }
    }

    @Test
    fun `when should quit immersive, then video settings should fade in`() = runBlockingTest(testDispatcher) {
        EventBusFactory.get(owner).emit(ScreenStateEvent::class.java, ScreenStateEvent.ImmersiveStateChanged(false))

        verifySequence { component.uiView.fadeIn() }
    }

    class VideoSettingsComponentMock(container: ViewGroup, bus: EventBusFactory, scope: CoroutineScope) : VideoSettingsComponent(container, bus, scope, TestCoroutineDispatchersProvider) {
        override fun initView(container: ViewGroup): VideoSettingsView {
            return mockk(relaxed = true)
        }
    }
}