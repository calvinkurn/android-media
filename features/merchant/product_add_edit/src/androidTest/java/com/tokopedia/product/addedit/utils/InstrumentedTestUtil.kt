package com.tokopedia.product.addedit.utils

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.product.addedit.R
import com.tokopedia.test.application.espresso_component.CommonMatcher
import org.hamcrest.Matchers

object InstrumentedTestUtil {

    fun performDialogSecondaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.dialog_btn_secondary)))
                .perform(ViewActions.click())
    }

    fun performDialogPrimaryClick() {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(R.id.dialog_btn_primary)))
                .perform(ViewActions.click())
    }

    fun performClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performScrollAndClick(id: Int) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.scrollTo())

        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.click())
    }

    fun performReplaceText(id: Int, text: String) {
        Espresso.onView(CommonMatcher
                .firstView(ViewMatchers.withId(id)))
                .perform(ViewActions.scrollTo())

        Espresso.onView(Matchers.allOf(ViewMatchers.withId(R.id.text_field_input), ViewMatchers.isDescendantOfA(ViewMatchers.withId(id))))
                .perform(ViewActions.typeText(text), ViewActions.closeSoftKeyboard())
    }

    fun performPressBack() {
        Espresso.pressBackUnconditionally()
    }
}