package com.tokopedia.picker.ui.gallery

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.picker.R
import com.tokopedia.picker.ui.core.GalleryPageTest
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class GalleryPageUITest : GalleryPageTest() {

    @Test
    fun should_show_media_list() {
        startPickerActivity()

        onView(
            withId(R.id.lst_media)
        ).check(matches(isDisplayed()))
    }

}