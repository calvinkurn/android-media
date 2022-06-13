package com.tokopedia.media.picker.ui.preview

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.di.TestPreviewInterceptor
import com.tokopedia.media.picker.ui.PreviewTest
import com.tokopedia.media.picker.ui.core.PreviewPageTest.Assert as PreviewAssert
import com.tokopedia.media.picker.ui.core.PreviewPageTest.Robot as PreviewRobot
import com.tokopedia.picker.common.EXTRA_RESULT_PICKER
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.picker.common.types.ModeType
import org.junit.Test

class PreviewPhotoOnlyUiTest : PreviewTest() {
    private val interceptor = TestPreviewInterceptor()

    override fun setUp() {
        super.setUp()
        previewComponent?.inject(interceptor)
    }

    @Test
    fun should_return_selected_media_onUploadClicked() {
        startPreviewPage()

        onView(withId(R.id.btn_done)).perform(click())
        val uploadResult =
            activityTestRule.activityResult.resultData.extras?.get(EXTRA_RESULT_PICKER) as PickerResult

        PreviewAssert.assertUploadImageResult(uploadResult)
    }

    @Test
    fun should_finish_preview_activity_onBackClicked() {
        startPreviewPage()

        onView(withId(R.id.btn_action)).perform(click())

        PreviewAssert.assertActivityFinish(this)
    }

    @Test
    fun should_match_drawer_item_size_with_provided_list_onInitialize() {
        startPreviewPage()

        PreviewAssert.assertImageListInitialization()
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

    private fun startPreviewPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam: PickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.Feed)
                it.modeType(ModeType.IMAGE_ONLY)
            }

        startPreviewActivity(pickerParam, IMAGE_MEDIA_ONLY)
    }
}