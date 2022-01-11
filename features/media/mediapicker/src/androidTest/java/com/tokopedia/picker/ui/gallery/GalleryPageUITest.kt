package com.tokopedia.picker.ui.gallery

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.picker.common.di.TestPickerInterceptor
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.ui.core.GalleryPageTest
import com.tokopedia.picker.utils.G500
import com.tokopedia.test.application.annotations.UiTest
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

    @Test
    fun should_show_media_list() {
        // Given
        val data = DataProvider.imageAndVideo

        interceptor.mockMedia(data)

        // When
        startPickerActivity()

        // Then
        Assertion.assertRecyclerViewDisplayed()
        Assertion.assertItemListSize(data.size)
    }

    @Test
    fun should_show_empty_media_list() {
        // Given
        val data = emptyList<Media>()

        interceptor.mockMedia(data)

        // When
        startPickerActivity()

        // Then
        Assertion.assertRecyclerViewDisplayed()
        Assertion.assertItemListSize(data.size)
    }

    @Test
    fun should_continue_button_is_active_mode_when_selected_media_item() {
        // Given
        val data = DataProvider.imageAndVideo

        interceptor.mockMedia(data)

        // When
        startPickerActivity()
        Action.clickFirstItemMediaList()

        /*
        * needed due event bus has debounce 500 millis
        * regarding the state of continue button
        * */
        Thread.sleep(500)

        // Then
        Assertion.assertContinueTextButtonColor(context, G500)
    }

}