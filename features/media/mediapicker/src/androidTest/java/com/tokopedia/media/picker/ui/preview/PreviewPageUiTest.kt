package com.tokopedia.media.picker.ui.preview

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.di.TestPreviewInterceptor
import com.tokopedia.media.picker.ui.PreviewTest
import com.tokopedia.media.picker.ui.core.PreviewPageTest
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.types.ModeType
import org.junit.Test

class PreviewPageUiTest : PreviewTest() {
    private val interceptor = TestPreviewInterceptor()

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

        PreviewPageTest.Assert.assertUploadVideoResult(uploadResult)
    }

    @Test
    fun should_finish_preview_activity_onBackClicked() {
        startPreviewPage()

        Espresso.onView(ViewMatchers.withId(R.id.btn_action)).perform(ViewActions.click())

        PreviewPageTest.Assert.assertActivityFinish(this)
    }

    @Test
    fun should_match_drawer_item_size_with_provided_list_onInitialize() {
        startPreviewPage()

        PreviewPageTest.Assert.assertVideoListInitialization()
    }

    @Test
    fun should_delete_drawer_item_onCloseClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.clickCloseThumbnailItem(0)

        PreviewPageTest.Assert.assertDeletedDrawerSelectionItem()
    }

    @Test
    fun should_delete_all_drawer_item_onCloseClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.clickCloseAllThumbnailItem()

        PreviewPageTest.Assert.assertActivityFinish(this)
    }

    @Test
    fun should_update_preview_item_onDrawerSelectionItemClicked() {
        startPreviewPage()

        val clickIndex = 0
        PreviewPageTest.Robot.clickThumbnailItem(clickIndex)

        PreviewPageTest.Assert.assertSelectedAndPreviewIndex(clickIndex)
    }

    @Test
    fun should_auto_play_if_media_video() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewPageTest.Assert.assertVideoIsPlay()
    }

    @Test
    fun should_stop_video_onPlayerPauseButtonClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewPageTest.Robot.clickPlayerPauseButton()

        PreviewPageTest.Assert.assertVideoIsPause()
    }

    @Test
    fun should_play_video_onPlayerPlayButtonClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewPageTest.Robot.clickPlayerPauseButton()
        PreviewPageTest.Robot.clickPlayerPlayButton()

        PreviewPageTest.Assert.assertVideoIsPlay()
    }

    @Test
    fun should_show_video_control_onPreviewViewClicked() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewPageTest.Robot.clickPreviewView()
        PreviewPageTest.Robot.clickPreviewView()
        PreviewPageTest.Assert.assertVideoControllerShow()
    }

    @Test
    fun should_move_video_time_onScrubberMove() {
        startPreviewPage()

        PreviewPageTest.Robot.waitExoPlayerInitialize()
        PreviewPageTest.Robot.clickVideoTimeBar()

        PreviewPageTest.Assert.assertVideoCurrentDuration()
    }

    private fun startPreviewPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam: PickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.Feed)
                it.modeType(ModeType.COMMON)
            }

        startPreviewActivity(pickerParam, COMBINATION_MEDIA)
    }
}