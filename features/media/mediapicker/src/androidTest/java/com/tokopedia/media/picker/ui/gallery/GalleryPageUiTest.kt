package com.tokopedia.media.picker.ui.gallery

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.ui.core.GalleryPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test

@UiTest
class GalleryPageUiTest : GalleryPageTest() {

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val interceptor = TestPickerInterceptor()

    private val imageFiles = mockImageFiles()
    private val videoFile = mockVideoFiles()

    override fun setUp() {
        super.setUp()
        pickerComponent?.inject(interceptor)
    }

    // ✅ should be able to fetch image only
    // ✅ should be able to fetch video only
    // ✅ should be able to fetch image and video
    // ⏳ should be able to single selection
    // ⏳ should be able to multiple selection with shown on drawer
    // ⏳ should be able to remove item on drawer
    // ⏳ should be able to select gallery item properly
    // ⏳ should be show toaster when selected item of lower minimum dimen of images
    // ⏳ should be show toaster when selected item of lower maximum dimen of images
    // ⏳ should be show toaster when selected item of max image file size

    @Test
    fun it_should_be_able_to_show_media_list() {
        // Given
        val imageAndVideo = imageFiles.plus(videoFile)
        interceptor.mockMedia(imageAndVideo)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertItemListSize(imageAndVideo.size)
    }

    @Test
    fun it_should_be_able_to_show_media_list_image_only() {
        // Given
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertItemListSize(imageFiles.size)
    }

    @Test
    fun it_should_be_able_to_show_media_list_video_only() {
        // Given
        interceptor.mockMedia(videoFile)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertItemListSize(videoFile.size)
    }

    @Test
    fun it_should_be_able_to_click_continue_button_when_particular_item_of_gallery_is_selected() {
        // Given
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage()
        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertContinueButtonIsVisible()
    }

    private fun startGalleryPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.CreatePost) // sample
                it.pageType(PageType.GALLERY)
            }

        startPickerActivity(pickerParam)
    }

}