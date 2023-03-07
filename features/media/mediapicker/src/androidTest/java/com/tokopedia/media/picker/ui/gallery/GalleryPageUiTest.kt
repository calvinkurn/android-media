package com.tokopedia.media.picker.ui.gallery

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.ui.core.GalleryPageTest
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

    override val isMultipleSelectionMode: Boolean
        get() = true

    override fun setUp() {
        super.setUp()
        pickerComponent?.inject(interceptor)
    }

    @Test
    fun should_be_able_to_show_empty_state() {
        // Given
        interceptor.mockMedia(listOf())

        // When
        startGalleryPage()

        // Then
        Asserts.assertEmptyStateDisplayed()
    }

    @Test
    fun should_be_able_to_show_media_list() {
        // Given
        interceptor.mockMedia(imageAndVideoFiles)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertMediaItemListSize(imageAndVideoFiles.size)
    }

    @Test
    fun should_be_able_to_show_media_list_image_only() {
        // Given
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertMediaItemListSize(mockImageFiles().size)
    }

    @Test
    fun should_be_able_to_show_media_list_video_only() {
        // Given
        interceptor.mockMedia(mockVideoFiles())

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertMediaItemListSize(mockVideoFiles().size)
    }

    @Test
    fun should_be_able_to_click_continue_button_when_first_item_is_selected() {
        // Given
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(0)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertContinueButtonIsVisible()
    }

}