package com.tokopedia.media.picker.ui.core

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.ui.activity.TestPreviewActivity
import com.tokopedia.media.picker.helper.utils.PickerCameraViewActions
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.activity.main.component.BottomNavComponent
import org.hamcrest.CoreMatchers.not

abstract class CameraPageTest : PickerTest() {

    object Robot {
        fun clickCaptureButton() {
            onView(
                withId(R.id.btn_take_camera)
            ).perform(click())
        }

        fun clickFlipCameraButton() {
            onView(
                withId(R.id.btn_flip)
            ).perform(click())
        }

        fun clickPreviewThumbnail() {
            Thread.sleep(5000)
            onView(
                withId(R.id.img_thumbnail)
            ).perform(click())
        }

        fun clickLanjutButton() {
            Thread.sleep(5000)
            onView(
                withId(R.id.btn_done)
            ).perform(click())
        }

        fun clickCloseButton() {
            onView(
                withId(R.id.btn_action)
            ).perform(click())
        }

        fun clickFlashButton(): Pair<Int, Int>? {
            var cameraRef: CameraView? = null

            onView(
                withId(R.id.cameraView)
            ).perform(
                PickerCameraViewActions.getFlashViewAction { viewRef ->
                    cameraRef = viewRef
                }
            )

            val initialFlashState = cameraRef?.flash?.ordinal

            onView(
                withId(R.id.btn_flash)
            ).perform(click())

            val latestFlashState = cameraRef?.flash?.ordinal

            return if (initialFlashState == null || latestFlashState == null)
                null
            else
                Pair(initialFlashState, latestFlashState)
        }

        fun clickCaptureVideo(duration: Long) {
            onView(
                withId(R.id.btn_take_camera)
            ).perform(PickerCameraViewActions.getRecordVideoViewAction(duration))
            Thread.sleep(duration)
        }

        fun clickGalleryTab() {
            onView(
                withId(R.id.tab_page)
            ).perform(
                PickerCameraViewActions.clickGalleryTabAction(BottomNavComponent.PAGE_GALLERY_INDEX)
            )
        }

        fun swipeLeftCameraMode() {
            onView(
                withId(R.id.lst_camera_mode)
            ).perform(swipeLeft())
        }
    }

    object Assert {
        fun assertCaptureImage() {
            Thread.sleep(2000)
            onView(
                withId(R.id.img_thumbnail)
            ).check(matches(isDisplayed()))
        }

        fun assertFlipCamera() {
            onView(
                withId(R.id.btn_flash)
            ).check(matches(not(isDisplayed())))
        }

        fun verifyOpenPreviewActivity() {
            Thread.sleep(3000)
            intended(hasComponent(TestPreviewActivity::class.java.name))
        }

        fun assertActivityDestroy(pickerTest: PickerTest) {
            Thread.sleep(2000)
            assert(pickerTest.activityTestRule.activity.isDestroyed)
        }

        fun assertGalleryFragment() {
            onView(
                withId(R.id.empty_state)
            ).check(matches(isDisplayed()))
        }
    }
}