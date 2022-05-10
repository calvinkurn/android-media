package com.tokopedia.media.picker.ui.camera

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType
import org.junit.Rule
import org.junit.Test

class CameraPageUiTest : CameraPageTest() {
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

    @Test
    fun should_show_thumbnail_from_captured_video_onCaptureButtonClicked() {
        // When
        startCameraPage()
        Robot.captureVideo(CAPTURED_VIDEO_DURATION)

        // Then
        Assert.assertCaptureImage()
    }

    @Test
    fun should_show_thumbnail_from_captured_photo_onCaptureButtonClicked() {
        // When
        startCameraPage()
        Robot.clickCaptureButton()

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
        Robot.clickCaptureButton()
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
    fun should_open_gallery_fragment_onGalleryTabClicked() {
        // When
        startCameraPage()
        Robot.clickGalleryTab()

        // Then
        Assert.assertGalleryFragment()
    }

    @Test
    fun should_open_preview_activity_onThumbnailClicker() {
        // When
        startCameraPage()
        Robot.clickCaptureButton()
        Robot.clickPreviewThumbnail()

        // Then
        Assert.verifyOpenPreviewActivity()
    }

    private fun startCameraPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam: PickerParam = PickerParam()
            .apply { param }
            .also {
                it.pageSource(PageSource.Feed)
                it.pageType(PageType.COMMON)
                it.modeType(ModeType.COMMON)
                it.minVideoDuration(VIDEO_MIN_DURATION)
            }

        startPickerActivity(pickerParam)
    }

    private companion object {
        private const val CAPTURED_VIDEO_DURATION = 2000L
        private const val VIDEO_MIN_DURATION = 1000
    }
}