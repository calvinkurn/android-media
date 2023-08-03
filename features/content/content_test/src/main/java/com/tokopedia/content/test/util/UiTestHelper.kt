package com.tokopedia.content.test.util

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.content.test.espresso.clickOnViewChild
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Created By : Jonathan Darwin on October 03, 2022
 */
fun click(@IdRes id: Int) {
    select(id).clickView()
}

fun click(text: String) {
    select(text).clickView()
}

fun click(@IdRes id: Int, middlewareAction: ViewInteraction.() -> Unit) {
    select(id).apply {
        middlewareAction()
    }.clickView()
}

fun clickWithMatcher(
    vararg matchers: Matcher<View>
) {
    onView(allOf(matchers.toList())).perform(click())
}

fun clickTabLayout(@IdRes id: Int, position: Int) {
    select(id).perform(
        object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return allOf(isDisplayed(), isAssignableFrom(TabsUnify::class.java))
            }

            override fun getDescription(): String {
                return "Select tab layout item on index $position"
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as TabsUnify).tabLayout.getTabAt(position)?.select()
            }
        }
    )
}

fun horizontalSlide(@IdRes id: Int, distance: Float) {
    select(id).perform(
        GeneralSwipeAction(
            Swipe.FAST,
            {
                val arr = IntArray(2)
                it.getLocationOnScreen(arr)

                floatArrayOf(arr[0].toFloat(), arr[1].toFloat())
            },
            {
                val arr = IntArray(2)
                it.getLocationOnScreen(arr)

                floatArrayOf(arr[0].toFloat() + distance, arr[1].toFloat())
            },
            Press.FINGER
        )
    )
}

fun type(@IdRes id: Int, text: String) {
    select(id).apply {
        clickView()
        perform(replaceText(text))
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

fun clickItemRecyclerView(@IdRes rvId: Int, position: Int, @IdRes id: Int) {
    select(rvId)
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, clickOnViewChild(id)
            )
        )
}

fun onItemRecyclerView(@IdRes rvId: Int, position: Int, action: (view: View) -> Unit) {
    select(rvId)
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, object : ViewAction {
                    override fun getConstraints() = null

                    override fun getDescription() = "Click on a child view with specific action"

                    override fun perform(uiController: UiController, view: View) {
                        action(view)
                    }
                }
            )
        )
}

fun clickItemOnNestedRecyclerView(
    @IdRes parentRvId: Int,
    parentPosition: Int,
    @IdRes childRvId: Int,
    childPosition: Int,
) {
    select(parentRvId)
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                parentPosition, object : ViewAction {
                    override fun getConstraints() = null

                    override fun getDescription() = "Click child on child recycler view"

                    override fun perform(uiController: UiController, view: View) {
                        val rv = view.findViewById<RecyclerView>(childRvId)
                        val selectedView = rv.getChildAt(childPosition)

                        click().perform(uiController, selectedView)
                    }
                }
            )
        )
}

fun verifyItemRecyclerView(@IdRes id: Int, position: Int, verifyBlock: (RecyclerView.ViewHolder) -> Boolean) {
    select(id)
        .check(
            matches(object: BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {

                }

                override fun matchesSafely(item: RecyclerView?): Boolean {
                    val viewHolder = item?.findViewHolderForAdapterPosition(position)
                    return viewHolder?.let {
                        verifyBlock(it)
                    } ?: false
                }
            })
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
) {
    select(id)
        .check(matches(object: BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description?) {

            }

            override fun matchesSafely(item: View?): Boolean {
                val view = item as? T
                return view?.let {
                    verifyBlock(it)
                } ?: false
            }
        }))
}

fun pressBack() {
    onView(isRoot()).perform(ViewActions.pressBack())
}

private fun select(@IdRes id: Int): ViewInteraction {
    return onView(withId(id))
}

private fun select(text: String): ViewInteraction {
    return onView(withText(text))
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
