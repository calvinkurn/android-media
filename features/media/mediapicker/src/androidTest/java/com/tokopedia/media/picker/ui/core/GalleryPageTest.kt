package com.tokopedia.media.picker.ui.core

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
import com.tokopedia.media.picker.ui.widget.drawerselector.viewholder.ThumbnailViewHolder
import com.tokopedia.picker.common.PageSource
import com.tokopedia.picker.common.PickerParam
import com.tokopedia.picker.common.types.PageType
import com.tokopedia.picker.common.utils.wrapper.PickerFile
import com.tokopedia.test.application.matcher.hasTotalItemOf
import org.hamcrest.Matcher

abstract class GalleryPageTest : PickerTest() {

    protected abstract val isMultipleSelectionMode: Boolean

    protected val imageAndVideoFiles = mockImageFiles()
        .plus(mockVideoFiles())

    fun mockImageFiles(): List<Media> {
        return ImageGenerator
            .getFiles(context)
            .mapIndexed { index, file ->
                Media(index.toLong(), file = PickerFile(filePath = file.path))
            }
    }

    fun mockVideoFiles(): List<Media> {
        return VideoGenerator
            .getFiles(context)
            .map {
                Media(Long.MAX_VALUE, file = PickerFile(filePath = it.path))
            }
    }

    protected open fun startGalleryPage(param: PickerParam.() -> Unit = {}) {
        val pickerParam = PickerParam()
            .apply(param)
            .also {
                it.pageSource(PageSource.CreatePost) // sample
                it.pageType(PageType.GALLERY)

                if (isMultipleSelectionMode) {
                    it.multipleSelectionMode()
                } else {
                    it.singleSelectionMode()
                }
            }

        startPickerActivity(pickerParam)
    }

    object Robot {
        fun clickContinueButtonOnToolbar() {
            onView(
                withId(R.id.action_text_done)
            ).perform(click())
        }

        fun clickRecyclerViewItemAt(position: Int) {
            onView(
                withRecyclerView(R.id.lst_media)
                    .atPosition(position)
            ).perform(click())
        }

        fun removeFirstItemOnDrawer() {
            onView(
                withId(R.id.iv_delete)
            ).perform(click())
        }
    }

    object Asserts {
        fun assertTextDisplayedWith(text: String) {
            // set waiting for 500ms before assertion
            Thread.sleep(500)

            onView(
                withText(text)
            ).check(
                matches(withEffectiveVisibility(Visibility.VISIBLE))
            )
        }

        fun assertRecyclerViewDisplayed() {
            onView(
                withId(R.id.lst_media)
            ).check(matches(isDisplayed()))
        }

        fun assertEmptyStateDisplayed() {
            onView(
                withId(R.id.empty_state)
            ).check(matches(isDisplayed()))
        }

        fun assertContinueButtonIsVisible() {
            onView(
                withId(R.id.action_text_done)
            ).check(matches(isDisplayed()))
        }

        fun assertMediaItemListSize(size: Int) {
            assertRecyclerviewItem(R.id.lst_media, hasTotalItemOf(size))
        }

        fun assertDrawerItemListSize(size: Int) {
            assertRecyclerviewItem(
                R.id.rv_thumbnail,
                hasTotalItemOf(size, ThumbnailViewHolder::class.java)
            )
        }

        private fun assertRecyclerviewItem(recyclerViewId: Int, matcher: Matcher<in View>) {
            onView(
                withId(recyclerViewId)
            ).check(matches(matcher))
        }
    }

}
