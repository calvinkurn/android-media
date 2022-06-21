package com.tokopedia.media.picker.ui.gallery

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.ui.core.GalleryPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.test.application.annotations.UiTest
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@UiTest
class GalleryPageUITest : GalleryPageTest() {

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    private val interceptor = TestPickerInterceptor()

    override fun setUp() {
        super.setUp()
        pickerComponent?.inject(interceptor)
    }

    // ⏳ should be able to fetch real media data
    // ⏳ should be able to fetch image only
    // ⏳ should be able to fetch video only
    // ⏳ should be able to fetch image and video
    // ⏳ should be able to fetch image and video with different album id
    // ⏳ should be able to single selection
    // ⏳ should be able to multiple selection with shown on drawer
    // ⏳ should be able to remove item on drawer

    @Test
    fun should_show_real_media_list() {
        runBlocking {
            // Given
            interceptor.realMedia(-1)

            // When
            startPickerActivity {
                pageSource(PageSource.CreatePost)
                pageType(PageType.GALLERY)
            }

            // Then
            Asserts.assertRecyclerViewDisplayed()
            Asserts.assertItemListSize(
                interceptor.mediaRepository.data.size
            )
        }
    }

    @Test
    fun should_show_mock_media_list() {
        // Given
        val data = DataProvider.imageAndVideo

        interceptor.mockMedia(data)

        // When
        startPickerActivity {
            pageSource(PageSource.CreatePost)
            pageType(PageType.GALLERY)
        }

        // Then
        Asserts.assertRecyclerViewDisplayed()
        Asserts.assertItemListSize(data.size)
    }

    @Test
    fun should_continue_button_is_active_mode_when_selected_media_item() {
        runBlocking {
            // Given
            interceptor.realMedia(-1)

            // When
            startPickerActivity {
                pageSource(PageSource.CreatePost)
                pageType(PageType.GALLERY)
            }

            Robot.clickFirstItemMediaList()

            /*
            * needed due event bus has debounce 500 millis
            * regarding the state of continue button
            * */
            Thread.sleep(500)

            // Then
            Asserts.assertContinueButtonIsVisible()
        }
    }

}