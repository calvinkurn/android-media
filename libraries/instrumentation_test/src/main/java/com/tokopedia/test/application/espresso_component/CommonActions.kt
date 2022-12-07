package com.tokopedia.test.application.espresso_component

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.tabs.TabLayout
import com.tokopedia.test.application.util.ViewUtils
import com.tokopedia.test.application.util.ViewUtils.takeScreenShot
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matcher

object CommonActions {

    const val UNDER_TEST_TAG = "UNDER_TEST_TAG"

    /**
     * Click on each item recyclerview actions.
     *
     * Best to use when:
     * You need to access all recyclerview children and you're dealing with multiple nested
     * horizontal recyclerview which causing Espresso AmbiguousViewMatcherException.
     *
     * This actions will only triggered the target recyclerview to prevent
     * Espresso AmbiguousViewMatcherException
     *
     * @param view view object in your espresso test
     * @param recyclerViewId id of recyclerview
     * @param fixedItemPositionLimit position limit when your recyclerview is endless recyclerview
     */
    fun clickOnEachItemRecyclerView(view: View, recyclerViewId: Int, fixedItemPositionLimit: Int) {
        val childRecyclerView: RecyclerView = view.findViewById(recyclerViewId)

        val tempStoreDesc = childRecyclerView.contentDescription
        childRecyclerView.contentDescription = UNDER_TEST_TAG

        var childItemCount = childRecyclerView.adapter!!.itemCount
        if (fixedItemPositionLimit > 0) {
            childItemCount = fixedItemPositionLimit
        }
        for (i in 0 until childItemCount) {
            try {
                Espresso.onView(
                    allOf(
                        ViewMatchers.withId(recyclerViewId),
                        ViewMatchers.withContentDescription(UNDER_TEST_TAG)
                    )
                )
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                            i,
                            ViewActions.click()
                        )
                    )
            } catch (e: PerformException) {
                e.printStackTrace()
            }
        }
        childRecyclerView.contentDescription = tempStoreDesc
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
                return allOf(
                    isDisplayed(),
                    ViewMatchers.isAssignableFrom(TabLayout::class.java)
                )
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

    /**
     * Display on specified child view inside of item recycler view
     *
     * example:
     * onView(withId(R.id.recycler_view)).check(matches(ViewMatchers.isDisplayed()))
     * .check(matches(displayChildViewWithId(index,id)))
     *
     * @param position index of list
     * @param targetViewId resource id of item
     */

    fun displayChildViewWithId(
        position: Int,
        targetViewId: Int,
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView?>(
            RecyclerView::class.java
        ) {


            override fun describeTo(description: org.hamcrest.Description) {

            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolder = item?.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder?.itemView?.findViewById<View>(targetViewId)
                return isDisplayed().matches(targetView)
            }

        }
    }

    /**
     * Display on specified child view inside of item recycler view
     *
     * example:
     * onView(withId(R.id.recycler_view)).check(matches(ViewMatchers.isDisplayed()))
     *  .check(matches(displayChildViewWithIdAndText(index,id,text)))
     *
     * @param position index of list
     * @param targetViewId resource id of item
     * @param text

     */

    fun displayChildViewWithIdAndText(
        position: Int,
        targetViewId: Int,
        text: String
    ): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView?>(
            RecyclerView::class.java
        ) {

            override fun describeTo(description: org.hamcrest.Description) {

            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolder = item?.findViewHolderForAdapterPosition(position)
                val targetView = viewHolder?.itemView?.findViewById<View>(targetViewId)
                return withText(text).matches(targetView) && isDisplayed().matches(targetView)
            }

        }
    }

    /**
     * Use for screenshot entire recyclerview into one image
     * @param startPosition determine in start position it will start screenshot
     * @param endPosition determine in start position it will end screenshot
     *        if you don't want to specify endPosition, pass adapter size
     * @param filePrefix name of the images .png file
     */
    fun screenShotFullRecyclerView(
        recyclerViewId: Int,
        startPosition: Int = 0,
        endPosition: Int,
        filePrefix: String
    ) {
        val views: MutableList<View?> = mutableListOf()
        getAllViewsViewHolder(recyclerViewId, startPosition, endPosition - 1) { view, index ->
            Thread.sleep(2000)
            views.add(view)
        }
        ViewUtils.mergeScreenShot(filePrefix, "", views)
    }

    /**
     * Will screenshot visible screen when instrument test run
     * @param view fragment or activity view
     */
    fun takeScreenShotVisibleViewInScreen(view: View, prefix: String, postfix: String) {
        view.takeScreenShot("$prefix-$postfix")
    }

    /**
     * Will screenshot partial view
     */
    fun findViewAndScreenShot(viewId: Int, fileName: String, fileNamePostFix: String) {
        Espresso.onView(allOf(ViewMatchers.withId(viewId)))
            .check(ViewAssertions.matches(isDisplayed())).perform(object : ViewAction {
                override fun getConstraints(): Matcher<View>? =
                    ViewMatchers.isAssignableFrom(View::class.java)

                override fun getDescription(): String {
                    return "getting text from a View"
                }

                override fun perform(uiController: UiController?, view: View?) {
                    view.takeScreenShot("$fileName-$fileNamePostFix")
                }
            })
    }

    fun findViewHolderAndDo(recyclerViewId: Int, position: Int, action: (View?) -> Unit) {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                screenShotChild { view ->
                    action.invoke(view)
                })
        )
    }

    /**
     * Will screenshot viewholder based on position, please make sure your data
     * @param shouldDelay if you need delay or wait (usually waiting for image to be inflate)
     */
    fun findViewHolderAndScreenshot(
        recyclerViewId: Int,
        position: Int,
        fileName: String,
        fileNamePostFix: String,
        shouldDelay: Boolean = false
    ) {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                screenShotChild { view ->
                    if (shouldDelay) {
                        Thread.sleep(5000)
                    }
                    view.takeScreenShot("$fileName-$fileNamePostFix")
                })
        )
    }

    private fun screenShotChild(listener: (View?) -> Unit): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? = null

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                listener.invoke(view)
            }
        }
    }

    private fun returnView(listener: (View?) -> Unit): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? = null

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                listener.invoke(view)
            }
        }
    }


    private fun getAllViewsViewHolder(
        recyclerViewId: Int,
        startPosition: Int,
        endPosition: Int,
        listener: (View?, Int) -> Unit
    ) {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(recyclerViewId))
            .check(ViewAssertions.matches(isDisplayed()))

        (startPosition..endPosition).forEach {
            viewInteraction.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    it,
                    returnView { view ->
                        if (view?.height != 0) {
                            listener.invoke(view, it)
                        }
                    })
            )
        }
    }
}
