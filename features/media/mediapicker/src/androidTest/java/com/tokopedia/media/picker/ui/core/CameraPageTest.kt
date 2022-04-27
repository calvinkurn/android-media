package com.tokopedia.media.picker.ui.core

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.otaliastudios.cameraview.CameraView
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.R
import com.tokopedia.media.picker.ui.activity.main.component.BottomNavComponent
import com.tokopedia.media.preview.ui.activity.PickerPreviewActivity
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

open class CameraPageTest : PickerTest() {
    override fun createAndAppendUri(builder: Uri.Builder) {}

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
            ).perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(CameraView::class.java)
                }

                override fun getDescription(): String {
                    return "get camera view reference"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    cameraRef = view as CameraView
                }
            })

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

        fun captureVideo(duration: Long) {
            onView(
                withId(R.id.lst_camera_mode)
            ).perform(swipeLeft())

            onView(
                withId(R.id.btn_take_camera)
            ).perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(View::class.java)
                }

                override fun getDescription(): String {
                    return "get capture btn"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    view?.performClick()
                    Handler(Looper.myLooper()!!).postDelayed({
                        view?.performClick()
                    }, duration)
                }
            })

            Thread.sleep(duration)
        }

        fun clickGalleryTab() {
            onView(
                withId(R.id.tab_page)
            ).perform(object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return ViewMatchers.isAssignableFrom(TabsUnify::class.java)
                }

                override fun getDescription(): String {
                    return "get picker bottom nav tab"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    val tab = view as TabsUnify
                    tab.getUnifyTabLayout().getTabAt(BottomNavComponent.PAGE_GALLERY_INDEX)
                        ?.select()
                }
            })
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
            intended(hasComponent(PickerPreviewActivity::class.java.name))
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