package com.tokopedia.media.picker.ui.gallery

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.common.ui.activity.TestPreviewActivity
import com.tokopedia.media.picker.ui.core.GalleryPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test
import kotlin.math.exp

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

    @Test
    fun should_be_able_to_show_media_list() {
        // Given
        val imageAndVideo = imageFiles.plus(videoFile)
        interceptor.mockMedia(imageAndVideo)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertMediaItemListSize(imageAndVideo.size)
    }

    @Test
    fun should_be_able_to_show_media_list_image_only() {
        // Given
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertMediaItemListSize(imageFiles.size)
    }

    @Test
    fun should_be_able_to_show_media_list_video_only() {
        // Given
        interceptor.mockMedia(videoFile)

        // When
        startGalleryPage()

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertMediaItemListSize(videoFile.size)
    }

    @Test
    fun should_be_able_to_click_continue_button_when_particular_item_of_gallery_is_selected() {
        // Given
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage {
            minImageResolution(0)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertContinueButtonIsVisible()
    }

    @Test
    fun should_be_able_to_single_selection_mode() {
        // Given
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage {
            singleSelectionMode()
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Thread.sleep(2000)

        intended(hasComponent(TestPreviewActivity::class.java.name))
    }

    @Test
    fun should_be_able_to_add_multiple_selection_on_drawer() {
        // Given
        val expectedItemsCount = 2

        interceptor.mockMedia(imageFiles)

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
        interceptor.mockMedia(imageFiles)

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
        val minImageDimen = 3000
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage {
            minImageResolution(minImageDimen)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_image_dimen() {
        // Given
        val maxImageDimen = 0
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage {
            maxImageResolution(maxImageDimen)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_image_file_size() {
        // Given
        val imageFileSize = 0L
        interceptor.mockMedia(imageFiles)

        // When
        startGalleryPage {
            maxImageFileSize(imageFileSize)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_reach_video_limit_selection() {
        // Given
        val maxVideoItem = 0
        interceptor.mockMedia(videoFile)

        // When
        startGalleryPage {
            maxVideoItem(maxVideoItem)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_min_video_duration() {
        // Given
        val minVideoDuration = 30_000
        interceptor.mockMedia(videoFile)

        // When
        startGalleryPage {
            minVideoDuration(minVideoDuration)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_video_duration() {
        // Given
        val maxVideoDuration = 0L
        interceptor.mockMedia(videoFile)

        // When
        startGalleryPage {
            maxVideoDuration(maxVideoDuration)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
    }

    @Test
    fun should_be_show_toaster_when_selected_item_lower_max_video_file_size() {
        // Given
        val maxVideoFileSize = 0L
        interceptor.mockMedia(videoFile)

        // When
        startGalleryPage {
            maxVideoFileSize(maxVideoFileSize)
        }

        Robot.clickRecyclerViewItemAt(0)

        // Then
        Asserts.assertDrawerItemListSize(0)
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