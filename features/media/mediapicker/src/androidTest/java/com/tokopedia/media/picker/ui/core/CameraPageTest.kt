package com.tokopedia.media.picker.ui.core

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.media.R
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import android.widget.TextView
import com.otaliastudios.cameraview.CameraView


open class CameraPageTest : PickerTest() {
    override fun createAndAppendUri(builder: Uri.Builder) {}

    object Robot {
        fun clickCaptureImageButton(){
            onView(
                withId(R.id.btn_take_camera)
            ).perform(click())
        }

        fun clickFlipCameraButton(){
            onView(
                withId(R.id.btn_flip)
            ).perform(click())
        }

        fun clickPreviewThumbnail() {
            onView(
                withId(R.id.img_thumbnail)
            ).perform(click())
        }

        fun clickLanjutButton(){
            onView(
                withId(R.id.btn_done)
            ).perform(click())
        }

        fun clickCloseButton(){
            onView(
                withId(R.id.btn_action)
            ).perform(click())
        }

        fun clickFlashButton() {
            onView(
                withId(R.id.btn_flash)
            ).perform(click())
        }
    }

    object Assert {
        fun assertCaptureImage() {
            onView(
                withId(R.id.img_thumbnail)
            ).check(matches(isDisplayed()))
        }

        fun assertFlipCamera(){
            onView(
                withId(R.id.btn_flash)
            ).check(matches(not(isDisplayed())))
        }

        fun assertPreviewThumbnail(){
            onView(
                withId(R.id.vp_preview)
            ).check(matches(isDisplayed()))
        }

        fun assertLanjutButton(){
            onView(
                withId(R.id.vp_preview)
            ).check(matches(isDisplayed()))
        }

        fun assertCloseButton(pickerTest: PickerTest){
            assert(pickerTest.activityTestRule.activity.isDestroyed)
        }

        fun assertFlashButton(imageView: ImageView){

        }
    }
}