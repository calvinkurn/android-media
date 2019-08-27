package com.tokopedia.logisticaddaddress.util

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.tkpd.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object EspressoUtils {
    fun rvHasItem(matcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("has item: ")
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val adapter = view.adapter!!
                for (position in 0 until adapter.itemCount) {
                    val type = adapter.getItemViewType(position)
                    val holder = adapter.createViewHolder(view, type)
                    adapter.onBindViewHolder(holder, position)
                    if (matcher.matches(holder.itemView)) {
                        return true
                    }
                }
                return false
            }
        }
    }

    fun itemWithText(title: String) = object : BoundedMatcher<View, View>(View::class.java) {
        override fun describeTo(description: Description?) {
            description?.appendText("Searching for title with: $title")
        }

        override fun matchesSafely(item: View?): Boolean {
            val views = ArrayList<View>()
            item?.findViewsWithText(views, title, View.FIND_VIEWS_WITH_TEXT)

            return when {
                views.size == 1 -> true
                else -> false
            }
        }
    }

    fun holderWithText(text: String): Matcher<RecyclerView.ViewHolder> {
        return object : TypeSafeMatcher<RecyclerView.ViewHolder>() {
            override fun describeTo(description: Description?) {
                description?.appendText("Searching for $text")
            }

            override fun matchesSafely(item: RecyclerView.ViewHolder?): Boolean {
                return item?.itemView?.findViewById<TextView>(R.id.address_name)?.text.toString() == text
            }
        }
    }

    fun childAtPosition(parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
