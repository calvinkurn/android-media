package com.tokopedia.media.picker.ui.internal

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.helper.ViewIdGenerator
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.utils.view.binding.internal.findRootView
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@UiTest
class CameraGenerateViewIdUiTest : CameraPageTest() {

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val interceptor = TestPickerInterceptor()

    @Before
    override fun setUp() {
        super.setUp()
        pickerComponent?.inject(interceptor)
    }

    @Test
    fun should_generate_view_id_on_camera() {
        // Given
        startCameraPage()

        // Then
        val rootView = findRootView(activityTestRule.activity)
        ViewIdGenerator.create(rootView, "mediapicker_camera.csv")
    }

    private fun startCameraPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam: PickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.Feed)
                it.pageType(PageType.COMMON)
                it.modeType(ModeType.COMMON)
                it.minVideoDuration()
            }

        startPickerActivity(pickerParam)
    }
}
