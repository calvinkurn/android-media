package com.tokopedia.attachproduct.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.attachproduct.view.viewholder.AttachProductListItemViewHolder
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

object ViewUtils {
    fun atPosition(position: Int, @NonNull itemMatcher: Matcher<View?>): Matcher<View?>? {
        checkNotNull(itemMatcher)
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder: RecyclerView.ViewHolder =
                    view.findViewHolderForAdapterPosition(position)
                        ?: // has no item on such position
                        return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun withCustomConstraints(action: ViewAction, constraints: Matcher<View>): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return constraints
            }

            override fun getDescription(): String {
                return action.description
            }

            override fun perform(uiController: UiController?, view: View?) {
                action.perform(uiController, view)
            }
        }
    }

    fun performClickOnProductCard(@IdRes recyclerViewId: Int) {
        val viewAction =
            RecyclerViewActions.actionOnItemAtPosition<AttachProductListItemViewHolder>(
                0, ViewActions.click()
            )
        Espresso.onView(AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.withId(recyclerViewId)))
            .perform(viewAction)
    }
}
