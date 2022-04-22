package com.tokopedia.media.picker.ui.camera

import androidx.test.rule.GrantPermissionRule
import com.tokopedia.media.picker.common.di.TestPickerInterceptor
import com.tokopedia.media.picker.ui.core.CameraPageTest
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.media.R
import com.tokopedia.picker.common.types.ModeType
import org.hamcrest.Matcher


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

    // V check capture image
    // V check flip camera
    // V check flash
    // V check capture image thumbnail click
    // V check click lanjut
    // V check click close

    @Test
    fun capture_image() {
        // When
        startCameraPage()
        Robot.clickCaptureImageButton()
        sleep(3000)

        // Then
        Assert.assertCaptureImage()
    }

    @Test
    fun check_flip_camera() {
        // When
        startCameraPage()
        Robot.clickFlipCameraButton()

        // Then
        Assert.assertFlipCamera()
    }

    @Test
    fun check_preview_thumbnail() {
        // When
        startCameraPage()
        Robot.clickCaptureImageButton()
        sleep(5000)
        Robot.clickPreviewThumbnail()

        // Then
        sleep(5000)
        Assert.assertPreviewThumbnail()
    }

    @Test
    fun check_lanjut_btn(){
        // When
        startCameraPage()
        Robot.clickCaptureImageButton()
        sleep(5000)
        Robot.clickLanjutButton()

        // Then
        sleep(5000)
        Assert.assertLanjutButton()
    }

    @Test
    fun check_close_btn(){
        // When
        startCameraPage()
        Robot.clickCloseButton()

        // Then
        sleep(1000)
        Assert.assertCloseButton(this)
    }

    // expected to fail on emulator that didn't support flash feature
    @Test
    fun check_flash_btn(){
        // When
        startCameraPage()

        var cameraRef: CameraView? = null
        Espresso.onView(
            ViewMatchers.withId(R.id.cameraView)
        ).perform(object: ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(CameraView::class.java)
            }

            override fun getDescription(): String {
                return "get camera view reference"
            }

            override fun perform(uiController: UiController?, view: View?) {
                cameraRef = view as CameraView
            }
        })

        val initialFlashState = cameraRef?.flash?.ordinal

        Robot.clickFlashButton()

        // Then
        assert(initialFlashState != cameraRef?.flash?.ordinal)
    }

    private fun startCameraPage() {
        val pickerParam: PickerParam.() -> Unit = {
            this.pageSource(PageSource.Feed)
            this.pageType(PageType.CAMERA)
            this.modeType(ModeType.IMAGE_ONLY)
        }

        startPickerActivity(pickerParam)
    }
}