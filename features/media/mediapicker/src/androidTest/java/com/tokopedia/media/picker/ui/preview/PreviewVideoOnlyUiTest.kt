package com.tokopedia.media.picker.ui.preview

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.di.TestPreviewInterceptor
import com.tokopedia.media.picker.ui.PreviewTest
import com.tokopedia.media.picker.ui.core.PreviewPageTest
import com.tokopedia.media.picker.ui.core.PreviewPageTest.Assert as PreviewAssert
import com.tokopedia.media.picker.ui.core.PreviewPageTest.Robot as PreviewRobot
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.types.ModeType
import org.junit.Before
import org.junit.Test

class PreviewVideoOnlyUiTest : PreviewTest() {
    private val interceptor = TestPreviewInterceptor()

    @Before
    override fun setUp() {
        super.setUp()
        previewComponent?.inject(interceptor)
    }

    @Test
    fun should_return_selected_media_onUploadClicked() {
        // Given
        startPreviewPage()

        // When
        Espresso.onView(ViewMatchers.withId(R.id.action_text_done)).perform(ViewActions.click())
        val uploadResult =
            activityTestRule.activityResult.resultData.extras?.get(EXTRA_RESULT_PICKER) as PickerResult

        // Then
        PreviewAssert.assertUploadVideoResult(uploadResult)
    }

    @Test
    fun should_finish_preview_activity_onBackClicked() {
        // Given
        startPreviewPage()

        // When
        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click())

        // Then
        PreviewAssert.assertActivityFinish(this)
    }

    @Test
    fun should_match_drawer_item_size_with_provided_list_onInitialize() {
        // Given
        startPreviewPage()

        // When

        // Then
        PreviewAssert.assertVideoListInitialization()
    }

    @Test
    fun should_delete_drawer_item_onCloseClicked() {
        // Given
        startPreviewPage()

        // When
        PreviewRobot.clickCloseThumbnailItem(0)

        // Then
        PreviewAssert.assertDeletedDrawerSelectionItem()
    }

    @Test
    fun should_delete_all_drawer_item_onCloseClicked() {
        // Given
        startPreviewPage()

        // When
        PreviewRobot.clickCloseAllThumbnailItem()

        // Then
        PreviewAssert.assertActivityFinish(this)
    }

    @Test
    fun should_update_preview_item_onDrawerSelectionItemClicked() {
        // Given
        startPreviewPage()
        val clickIndex = 0

        // When
        PreviewRobot.clickThumbnailItem(clickIndex)

        // Then
        PreviewAssert.assertSelectedAndPreviewIndex(clickIndex)
    }

    @Test
    fun should_auto_play_if_media_video() {
        // Given
        startPreviewPage()

        // When
        PreviewPageTest.Robot.waitExoPlayerInitialize()

        // Then
        PreviewAssert.assertVideoIsPlay()
    }

    @Test
    fun should_stop_video_onPlayerPauseButtonClicked() {
        // Given
        startPreviewPage()

        // When
        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickPlayerPauseButton()

        // Then
        PreviewAssert.assertVideoIsPause()
    }

    @Test
    fun should_play_video_onPlayerPlayButtonClicked() {
        // Given
        startPreviewPage()

        // When
        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickPlayerPauseButton()
        PreviewRobot.clickPlayerPlayButton()

        // Then
        PreviewAssert.assertVideoIsPlay()
    }

    @Test
    fun should_show_video_control_onPreviewViewClicked() {
        // Given
        startPreviewPage()

        // When
        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickPreviewView()
        PreviewRobot.clickPreviewView()

        // Then
        PreviewAssert.assertVideoControllerShow()
    }

    @Test
    fun should_move_video_time_onScrubberMove() {
        // Given
        startPreviewPage()

        // When
        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickVideoTimeBar()

        // Then
        PreviewAssert.assertVideoCurrentDuration()
    }

    private fun startPreviewPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam: PickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.Feed)
                it.modeType(ModeType.VIDEO_ONLY)
            }

        startPreviewActivity(pickerParam, VIDEO_MEDIA_ONLY)
    }
}
