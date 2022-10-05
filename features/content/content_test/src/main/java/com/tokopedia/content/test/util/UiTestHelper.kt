package com.tokopedia.content.test.util

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
fun select(@IdRes id: Int): ViewInteraction {
    return onView(withId(id))
}

fun select(text: String): ViewInteraction {
    return onView(withText(text))
}

fun click(@IdRes id: Int) {
    select(id).clickView()
}

fun click(text: String) {
    select(text).clickView()
}

fun click(@IdRes parentId: Int, text: String) {
    val matcher = allOf(withText(text), isDescendantOfA(withId(parentId)))
    onView(matcher).perform(click())
}

fun click(
    @IdRes parentId: Int,
    @IdRes id: Int,
) {
    val matcher = allOf(withId(id), isDescendantOfA(withId(parentId)))
    onView(matcher).perform(click())
}

fun type(@IdRes id: Int, text: String) {
    select(id).apply {
        clickView()
        perform(typeText(text))
    }
}

fun pressActionSoftKeyboard(@IdRes id: Int) {
    select(id).perform(pressImeActionButton())
}

fun selectTag(tag: String): ViewInteraction {
    return onView(withTagValue(`is`(tag)))
}

fun isVisible(@IdRes vararg ids: Int) {
    ids.forEach { id -> select(id).isVisible() }
}

fun isHidden(@IdRes vararg ids: Int) {
    ids.forEach { id -> select(id).isHidden() }
}

fun clickItemRecyclerView(@IdRes id: Int, position: Int) {
    select(id)
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, click()
            )
        )
}

fun verifyItemRecyclerView(@IdRes id: Int, position: Int, verifyBlock: (RecyclerView.ViewHolder) -> Boolean) {
    select(id)
        .check(
            matches(atPosition(position, verifyBlock))
        )
}

fun verifyText(@IdRes id: Int, text: String) {
    select(id).verifyText(text)
}

fun verifyButtonState(@IdRes id: Int, isEnabled: Boolean) {
    select(id).check(
        matches(
            if(isEnabled) isEnabled()
            else not(isEnabled())
        )
    )
}

fun <T: View> verify(
    @IdRes id: Int,
    verifyBlock: (T) -> Boolean,
): Matcher<View> {
    return object: BoundedMatcher<View, View>(View::class.java) {
        override fun describeTo(description: Description?) {

        }

        override fun matchesSafely(item: View?): Boolean {
            val view = item as? T
            return view?.let {
                verifyBlock(it)
            } ?: false
        }
    }
}

private fun ViewInteraction.clickView() {
    perform(click())
}

private fun ViewInteraction.isVisible() {
    check(matches(isDisplayed()))
}

private fun ViewInteraction.isHidden() {
    check(matches(not(isDisplayed())))
}

private fun ViewInteraction.verifyText(text: String) {
    check(matches(withText(text)))
}

private fun atPosition(
    position: Int,
    verifyBlock: (RecyclerView.ViewHolder) -> Boolean
): Matcher<View> {
    return object: BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description?) {

        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val viewHolder = item?.findViewHolderForAdapterPosition(position)
            return viewHolder?.let {
                verifyBlock(it)
            } ?: false
        }
    }
}