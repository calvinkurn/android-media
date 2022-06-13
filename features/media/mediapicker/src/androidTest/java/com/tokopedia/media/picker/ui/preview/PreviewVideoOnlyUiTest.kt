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
        startPreviewPage()

        Espresso.onView(ViewMatchers.withId(R.id.btn_done)).perform(ViewActions.click())
        val uploadResult =
            activityTestRule.activityResult.resultData.extras?.get(EXTRA_RESULT_PICKER) as PickerResult

        PreviewAssert.assertUploadVideoResult(uploadResult)
    }

    @Test
    fun should_finish_preview_activity_onBackClicked() {
        startPreviewPage()

        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click())

        PreviewAssert.assertActivityFinish(this)
    }

    @Test
    fun should_match_drawer_item_size_with_provided_list_onInitialize() {
        startPreviewPage()

        PreviewAssert.assertVideoListInitialization()
    }

    @Test
    fun should_delete_drawer_item_onCloseClicked() {
        startPreviewPage()

        PreviewRobot.clickCloseThumbnailItem(0)

        PreviewAssert.assertDeletedDrawerSelectionItem()
    }

    @Test
    fun should_delete_all_drawer_item_onCloseClicked() {
        startPreviewPage()

        PreviewRobot.clickCloseAllThumbnailItem()

        PreviewAssert.assertActivityFinish(this)
    }

    @Test
    fun should_update_preview_item_onDrawerSelectionItemClicked() {
        startPreviewPage()

        val clickIndex = 0
        PreviewRobot.clickThumbnailItem(clickIndex)

        PreviewAssert.assertSelectedAndPreviewIndex(clickIndex)
    }

    @Test
    fun should_auto_play_if_media_video() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewAssert.assertVideoIsPlay()
    }

    @Test
    fun should_stop_video_onPlayerPauseButtonClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickPlayerPauseButton()

        PreviewAssert.assertVideoIsPause()
    }

    @Test
    fun should_play_video_onPlayerPlayButtonClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickPlayerPauseButton()
        PreviewRobot.clickPlayerPlayButton()

        PreviewAssert.assertVideoIsPlay()
    }

    @Test
    fun should_show_video_control_onPreviewViewClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickPreviewView()
        PreviewRobot.clickPreviewView()
        PreviewAssert.assertVideoControllerShow()
    }

    @Test
    fun should_move_video_time_onScrubberMove() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewRobot.clickVideoTimeBar()

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