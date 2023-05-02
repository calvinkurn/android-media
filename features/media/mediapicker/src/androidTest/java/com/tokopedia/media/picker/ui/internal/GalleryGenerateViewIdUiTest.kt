package com.tokopedia.media.picker.ui.internal

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.helper.ViewIdGenerator
import com.tokopedia.media.picker.ui.core.GalleryPageTest
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Rule
import org.junit.Test

@UiTest
class GalleryGenerateViewIdUiTest : GalleryPageTest() {

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
    fun should_generate_view_id_on_gallery() {
        // Given
        startGalleryPage()

        // Then
        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.create(rootView, "mediapicker_gallery.csv")
    }

}
