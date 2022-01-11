package com.tokopedia.picker.ui.core

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.helper.matchers.withRecyclerView
import com.tokopedia.picker.helper.matchers.withTextColor
import com.tokopedia.picker.ui.PickerTest
import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.unifycomponents.UnifyButton
import org.hamcrest.Matcher

open class GalleryPageTest : PickerTest() {

    override fun buildUri(builder: Uri.Builder) {
        builder.appendQueryParameter(
            ApplinkConst.MediaPicker.PARAM_PAGE,
            ApplinkConst.MediaPicker.VALUE_PAGE_GALLERY
        )
    }

    object DataProvider {
        val imageOnly = listOf(
            Media(1, "sample.png", ""),
            Media(2, "sample.png", ""),
        )

        val videoOnly = listOf(
            Media(1, "videoplayback.mp4", ""),
            Media(2, "videoplayback.mp4", ""),
        )

        val imageAndVideo = listOf(
            Media(1, "sample.png", ""),
            Media(2, "videoplayback.mp4", ""),
        )
    }

    object Action {
        fun clickContinueButtonOnToolbar() {
            onView(
                withId(R.id.btn_done)
            ).perform(click())
        }

        fun clickFirstItemMediaList() {
            onView(
                withRecyclerView(R.id.lst_media)
                    .atPosition(0)
            ).perform(click())
        }
    }

    object Assertion {
        fun assertRecyclerViewDisplayed() {
            onView(
                withId(R.id.lst_media)
            ).check(matches(isDisplayed()))
        }

        fun assertContinueTextButtonColor(context: Context, color: Int) {
            val textColor = ContextCompat.getColor(context, color)

            onView(
                withId(R.id.btn_done)
            ).check(matches(withTextColor<UnifyButton>(textColor)))
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