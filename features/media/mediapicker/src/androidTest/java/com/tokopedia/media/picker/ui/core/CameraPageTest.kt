package com.tokopedia.media.picker.ui.core

import android.net.Uri
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.R
import com.tokopedia.media.picker.common.ui.activity.TestPreviewActivity
import com.tokopedia.media.picker.helper.utils.PickerCameraViewActions
import com.tokopedia.media.picker.ui.activity.main.component.BottomNavComponent
import org.hamcrest.CoreMatchers.not
import okhttp3.internal.notify


open class CameraPageTest : PickerTest() {
    override fun createAndAppendUri(builder: Uri.Builder) {}

    object Robot {
        fun clickCapturePhoto() {
            synchronized(this){
                onView(
                    withId(R.id.btn_take_camera)
                ).perform(click())

                threadPause()
            }
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
                withId(R.id.btn_done)
            ).perform(click())
        }

        fun clickCloseButton() {
            synchronized(this){
                onView(
                    withId(R.id.btn_action)
                ).perform(click())

                threadPause()
            }
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
            synchronized(this){
                onView(
                    withId(R.id.btn_take_camera)
                ).perform(PickerCameraViewActions.getRecordVideoViewAction(duration))
                Thread.sleep(duration)

                threadPause()
            }
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

        fun resumeThread() {
            synchronized(this){
                Thread.sleep(THUMBNAIL_LOADED_DELAY)
                notify()
            }
        }

        private fun threadPause() {
            (this as Object).wait(TIMEOUT_DURATION)
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
            intended(hasComponent(TestPreviewActivity::class.java.name))
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

    companion object{
        const val TIMEOUT_DURATION = 5000L
        const val THUMBNAIL_LOADED_DELAY = 1000L
    }
}