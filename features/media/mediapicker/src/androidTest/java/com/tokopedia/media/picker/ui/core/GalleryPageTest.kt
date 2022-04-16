package com.tokopedia.media.picker.ui.core

import android.net.Uri
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.media.R
import com.tokopedia.media.picker.data.entity.Media
import com.tokopedia.media.picker.helper.matchers.withRecyclerView
import com.tokopedia.media.picker.helper.utils.ImageGenerator
import com.tokopedia.media.picker.helper.utils.VideoGenerator
import com.tokopedia.media.picker.ui.PickerTest
import com.tokopedia.test.application.matcher.hasTotalItemOf
import org.hamcrest.Matcher

open class GalleryPageTest : PickerTest() {

    override fun createAndAppendUri(builder: Uri.Builder) {}

    fun mockImageFiles(): List<Media> {
        val files = ImageGenerator.getFiles(context)
        var mockMediaId = 0L

        return files.map {
            mockMediaId++

            Media(mockMediaId, it.name, it.path)
        }
    }

    fun mockVideoFiles(): List<Media> {
        val videoFile = VideoGenerator.getFiles(context)
        val mockMediaId = Long.MAX_VALUE

        return videoFile.map {
            Media(mockMediaId, it.name, it.path)
        }
    }

    object Robot {
        fun clickContinueButtonOnToolbar() {
            onView(
                withId(R.id.btn_done)
            ).perform(click())
        }

        fun clickRecyclerViewItemAt(position: Int) {
            onView(
                withRecyclerView(R.id.lst_media)
                    .atPosition(position)
            ).perform(click())
        }
    }

    object Asserts {
        fun assertToasterIsShownWithText(text: String) {
            onView(
                withId(R.id.snackbar_txt)
            ).check(matches(withText(text)))
        }

        fun assertRecyclerViewDisplayed() {
            onView(
                withId(R.id.lst_media)
            ).check(matches(isDisplayed()))
        }

        fun assertContinueButtonIsVisible() {
            onView(
                withId(R.id.btn_done)
            ).check(matches(isDisplayed()))
        }

        fun assertItemListSize(size: Int) {
            assertRecyclerviewItem(hasTotalItemOf(size))
        }

        private fun assertRecyclerviewItem(matcher: Matcher<in View>) {
            onView(
                withId(R.id.lst_media)
            ).check(matches(matcher))
        }
    }

}