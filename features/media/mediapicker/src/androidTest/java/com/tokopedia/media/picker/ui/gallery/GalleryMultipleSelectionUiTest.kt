package com.tokopedia.media.picker.ui.gallery

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.helper.utils.toSec
import com.tokopedia.media.picker.ui.core.GalleryPageTest
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test

@UiTest
class GalleryMultipleSelectionUiTest : GalleryPageTest() {

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
    fun should_be_able_to_add_multiple_selection_on_drawer() {
        // Given
        val expectedItemsCount = 2

        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(0)
        }

        Robot.clickRecyclerViewItemAt(0)
        Robot.clickRecyclerViewItemAt(1)

        // Then
        Asserts.assertDrawerItemListSize(expectedItemsCount)
    }

    @Test
    fun should_be_able_to_remove_item_on_drawer() {
        // Given
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(0)
        }

        Robot.clickRecyclerViewItemAt(0)
        Robot.removeFirstItemOnDrawer()

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_min_image_dimen() {
        // Given
        val minImageDimen = 3000 // simulate the dimens as min image dimen threshold
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(minImageDimen)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_image_res_min_limit, minImageDimen)
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_image_dimen() {
        // Given
        val maxImageDimen = -1 // simulate the dimens as max image dimen threshold
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(0)
            maxImageResolution(maxImageDimen)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_image_res_max_limit, maxImageDimen)
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_image_file_size() {
        // Given
        val imageFileSize = 0L // simulate the image file size as lower threshold
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(0)
            maxImageFileSize(imageFileSize)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_image_max_size, imageFileSize)
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_reach_video_limit_selection() {
        // Given
        val maxVideoItem = 0
        interceptor.mockMedia(mockVideoFiles())

        // When
        startGalleryPage {
            maxVideoItem(maxVideoItem)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_selection_limit_video, maxVideoItem)
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_reach_media_limit_selection() {
        // Given
        val maxMediaItem = 3
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage {
            minImageResolution(0)
            maxMediaItem(maxMediaItem)
            maxVideoItem(0)
        }

        for (i in 0..maxMediaItem) {
            Robot.clickRecyclerViewItemAt(i)
            Thread.sleep(500)
        }

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_selection_limit_message, maxMediaItem)
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_min_video_duration() {
        // Given
        val minVideoDuration = 30_000 // simulate the video duration as lower threshold
        interceptor.mockMedia(mockVideoFiles())

        // When
        startGalleryPage {
            minVideoDuration(minVideoDuration)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_video_duration_min_limit, minVideoDuration.toSec())
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_video_duration() {
        // Given
        val maxVideoDuration = 0 // simulate the video duration as higher threshold
        interceptor.mockMedia(mockVideoFiles())

        // When
        startGalleryPage {
            minVideoDuration(0)
            maxVideoDuration(maxVideoDuration)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(
                R.string.picker_video_duration_max_limit,
                maxVideoDuration.toSec().humanize(context)
            )
        )
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_video_file_size() {
        // Given
        val maxVideoFileSize = 0L
        interceptor.mockMedia(mockVideoFiles())

        // When
        startGalleryPage {
            maxVideoFileSize(maxVideoFileSize)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertTextDisplayedWith(
            context.getString(R.string.picker_video_max_size, maxVideoFileSize)
        )
    }

}