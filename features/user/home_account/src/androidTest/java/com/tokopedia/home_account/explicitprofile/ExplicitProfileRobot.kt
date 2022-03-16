package com.tokopedia.home_account.explicitprofile

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.home_account.test.R
import com.tokopedia.iconunify.IconUnify
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not


fun explicitProfileRobot(action: ExplicitProfileRobot.() -> Unit): ExplicitProfileRobot {
    return ExplicitProfileRobot().apply(action)
}

infix fun ExplicitProfileRobot.validateComponent(action: ExplicitProfileResult.() -> Unit): ExplicitProfileResult {
    Thread.sleep(2500)
    return ExplicitProfileResult().apply(action)
}

class ExplicitProfileRobot {

    fun clickAnswerWithText(text: String) {
        onView(allOf(withText(text), isDisplayed()))
            .perform(click())
    }

    fun selectTabWithText(text: String) {
        onView(withText(text))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun swipeTabLeft() {
        onView(withId(R.id.tabCategories))
            .perform(swipeLeft())
    }

    fun swipeTabRight() {
        onView(withId(R.id.tabCategories))
            .perform(swipeRight())
    }

    fun swipeViewPagerLeft() {
        onView(withId(R.id.pagerCategories))
            .perform(swipeLeft())
    }

    fun swipeViewPagerRight() {
        onView(withId(R.id.pagerCategories))
            .perform(swipeRight())
    }

    fun clickOnInfoSection() {
        onView(allOf(withId(R.id.sectionInfoIcon), isDisplayed()))
            .perform(clickOnUnifyIcon())
    }
}

class ExplicitProfileResult {

    fun shouldButtonSaveEnabled() {
        onView(withId(R.id.btnSave))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    fun shouldButtonSaveDisabled() {
        onView(withId(R.id.btnSave))
            .check(matches(isDisplayed()))
            .check(matches(not(isEnabled())))
    }

    fun shouldViewEmptyPage() {
        onView(allOf(
            withId(R.id.titleEmptyPage),
            withText("Kategori lainnya masih disiapin"),
            isDisplayed())
        )
        onView(
            allOf(
                withId(R.id.descriptionEmptyPage),
                withText("Nanti kamu bisa atur preferensi belanja buat kategori selain kuliner di sini, lho!"),
                isDisplayed()
            )
        )
    }

    fun shouldViewBottomSheetSectionInfo() {
        onView(withText("Info")).check(matches(isDisplayed()))
        onView(withText("Tidak mengandung babi, alkohol, dan bahan non-halal lainnya."))
            .check(matches(isDisplayed()))
    }
}

fun clickOnUnifyIcon() : ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return allOf(withId(R.id.sectionInfoIcon), isDisplayed())
        }

        override fun getDescription(): String {
            return "Click on icon unify"
        }

        override fun perform(uiController: UiController?, view: View?) {
            (view as IconUnify).performClick()
        }

    }
}