package com.tokopedia.media.picker.ui.core

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.google.android.gms.tagmanager.PreviewActivity
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.media.R
import com.tokopedia.media.picker.helper.utils.PickerCameraViewActions
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.picker.ui.component.BottomNavComponent
import org.hamcrest.CoreMatchers.not

abstract class CameraPageTest : PickerTest() {
    object Robot {
        fun clickCapturePhoto() {
            onView(
                withId(R.id.btn_take_camera)
            ).perform(click())

            countingIdlingResource.increment()
        }

        fun clickFlipCameraButton() {
            onView(
                withId(R.id.btn_flip)
            ).perform(click())
        }

        fun clickPreviewThumbnail() {
            onView(
                withId(R.id.img_thumbnail)
            ).perform(click())
        }

        fun clickLanjutButton() {
            onView(
                withId(R.id.action_text_done)
            ).perform(click())
        }

        fun clickCloseButton() {
            onView(
                withId(R.id.btn_action)
            ).perform(click())

            Thread.sleep(1000)
        }

        fun clickFlashButton(): Pair<Int, Int>? {
            var cameraRef: CameraView? = null

            onView(
                withId(R.id.cameraView)
            ).check { view, _ ->
                cameraRef = view as CameraView
            }

            /**
             * supported flash 1 means flash only support OFF,
             * that means device didn't have flash
             */
            if (cameraRef?.cameraOptions?.supportedFlash?.size == 1) {
                return null
            }

            val initialFlashState = cameraRef?.flash?.ordinal


            onView(
                withId(R.id.btn_flash)
            ).perform(click())

            var latestFlashState = cameraRef?.flash?.ordinal

            return Pair(initialFlashState ?: 0, latestFlashState ?: -1)
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

        fun decrement() {
            if (!countingIdlingResource.isIdleNow) countingIdlingResource.decrement()
        }
    }

    object Assert {
        fun assertCaptureImage() {
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
            intended(hasComponent(PreviewActivity::class.java.name))
        }

        fun assertActivityDestroy(pickerTest: PickerTest) {
            assert(pickerTest.activityTestRule.activity.isDestroyed)
        }

        fun assertGalleryFragment() {
            onView(
                withId(R.id.empty_state)
            ).check(matches(isDisplayed()))
        }
    }

    companion object {
        val countingIdlingResource = CountingIdlingResource("CameraPageIdlingResource")
    }
}
