package com.tokopedia.media.picker.ui.gallery

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.tagmanager.PreviewActivity
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.common.ui.activity.TestPreviewActivity
import com.tokopedia.media.picker.helper.utils.toSec
import com.tokopedia.media.picker.ui.core.GalleryPageTest
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.picker.common.mapper.humanize
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test

@UiTest
class GallerySingleSelectionUiTest : GalleryPageTest() {

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val interceptor = TestPickerInterceptor()

    override val isMultipleSelectionMode: Boolean
        get() = false

    override fun setUp() {
        super.setUp()
        pickerComponent?.inject(interceptor)
    }

    @Test
    fun should_be_able_to_single_selection_mode_of_image() {
        // Given
        interceptor.mockMedia(mockImageFiles())

        // When
        startGalleryPage()

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(PreviewActivity::class.java.name))
    }

    @Test
    fun should_be_able_to_single_selection_mode_of_video() {
        // Given
        interceptor.mockMedia(mockVideoFiles())

        // When
        startGalleryPage {
            minVideoDuration(0)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Thread.sleep(2000)

        Intents.intended(IntentMatchers.hasComponent(PreviewActivity::class.java.name))
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