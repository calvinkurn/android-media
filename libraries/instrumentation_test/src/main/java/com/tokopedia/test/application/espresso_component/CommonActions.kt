package com.tokopedia.test.application.espresso_component

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import com.tokopedia.unifycomponents.TabsUnify
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf

object CommonActions {
    /**
     * Click on each item recyclerview actions.
     *
     * Best to use when:
     * You need to access all recyclerview children and you're dealing with multiple nested
     * horizontal recyclerview which causing Espresso AmbiguousViewMatcherException.
     *
     * This actions will only triggered the firstView of recyclerview inside on viewport to prevent
     * Espresso AmbiguousViewMatcherException
     *
     * @param view view object in your espresso test
     * @param recyclerViewId id of recyclerview
     * @param fixedItemPositionLimit position limit when your recyclerview is endless recyclerview
     */
    fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int, fixedItemPositionLimit: Int) {
        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)
        var childItemCount = childRecyclerView.adapter!!.itemCount
        if (fixedItemPositionLimit > 0) {
            childItemCount = fixedItemPositionLimit
        }
        for (i in 0 until childItemCount) {
            try {
                Espresso.onView(CommonMatcher.firstView(ViewMatchers.withId(recyclerViewId)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
            } catch (e: PerformException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Click on one of the tab in TabLayout
     *
     * Best to use when: layout component is TabLayout
     * But if your layout component is TabsUnify, don't use this method to select tab. Better to use:
     * onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("[Title tab]"))).perform(click())
     *
     * @param tabIndex index of tab
     */
    fun selectTabLayoutPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "with tab at index $tabIndex"
            }

            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TabLayout::class.java))
            }

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()
                tabAtIndex.select()
            }
        }
    }

    /**
     * Click on specified child view inside of item recycler view
     *
     * example:
     * onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<ViewHolderClass>(
     * 1,CommonActions.clickChildViewWithId(R.id.see_more_btn)))
     *
     * @param id resource id of item action
     */
    fun clickChildViewWithId(id: Int): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val v: View = view.findViewById(id)
                v.performClick()
            }
        }
    }
}