package com.tokopedia.media.picker.ui.camera

import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test
import com.tokopedia.picker.common.types.ModeType
import org.junit.After
import org.junit.Before

@UiTest
class CameraPhotoOnlyUiTest : CameraPageTest() {

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val interceptor = TestPickerInterceptor()

    override fun setUp() {
        super.setUp()
        pickerComponent?.inject(interceptor)
    }

    @Before
    fun setIdlingResource(){
        IdlingRegistry.getInstance().register(Robot.countingIdlingResource)
    }

    @After
    fun releaseIdlingResource(){
        IdlingRegistry.getInstance().unregister(Robot.countingIdlingResource)
    }

    @Test
    fun should_show_thumbnail_from_captured_photo_onCaptureButtonClicked() {
        // When
        startCameraPage()
        Robot.clickCapturePhoto()

        // Then
        Assert.assertCaptureImage()
    }

    @Test
    fun should_flip_camera_and_hide_flash_button_onFlipButtonClicked() {
        // When
        startCameraPage()
        Robot.clickFlipCameraButton()

        // Then
        Assert.assertFlipCamera()
    }

    @Test
    fun should_close_activity_onCloseClicked() {
        // When
        startCameraPage()
        Robot.clickCloseButton()

        // Then
        Assert.assertActivityDestroy(this)
    }

    @Test
    fun should_open_preview_activity_onLanjutClicked() {
        // When
        startCameraPage()
        Robot.clickCapturePhoto()
        Robot.clickLanjutButton()

        // Then
        Assert.verifyOpenPreviewActivity()
    }

    @Test
    fun should_update_flash_state_onFlashButtonClicked() {
        // When
        startCameraPage()
        val flashState = Robot.clickFlashButton()

        // Then
        assert(flashState != null)
        assert(flashState?.first != flashState?.second)
    }

    @Test
    fun should_open_preview_activity_onThumbnailClicked() {
        // When
        startCameraPage()
        Robot.clickCapturePhoto()
        Robot.clickPreviewThumbnail()

        // Then
        Assert.verifyOpenPreviewActivity()
    }

    private fun startCameraPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.Feed)
                it.pageType(PageType.CAMERA)
                it.modeType(ModeType.IMAGE_ONLY)
            }

        startPickerActivity(pickerParam)
    }
}