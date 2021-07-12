package com.tokopedia.buyerorder

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.buyerorder.test.R
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import org.hamcrest.*


/**
 * Created by fwidjaja on 08/11/20.
 */
class UohRobot {
    fun loading() {
        waitForData()
    }

    fun clickPrimaryButton() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_order_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        clickOnViewChild(com.tokopedia.buyerorder.R.id.uoh_btn_action)))
    }

    fun clickThreeDotsMenu() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_order_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        clickOnViewChild(com.tokopedia.buyerorder.R.id.iv_kebab_menu)))
    }

    fun clickBeliLagi() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_kebab))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun clickOrderCard() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_order_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        clickOnViewChild(com.tokopedia.buyerorder.R.id.cl_data_product)))
        pressBack()
    }

    fun doSearch(str: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield))
                .perform(ViewActions.typeText(str)).perform(ViewActions.pressImeActionButton())
        waitForData()
    }

    fun doApplyFilter() {
        onView(withId(com.tokopedia.buyerorder.R.id.btn_apply))
                .perform(click())
        waitForData()
    }

    fun clickFilterStatus() {
        onView(nthChildOf(withId(com.tokopedia.sortfilter.R.id.sort_filter_items),0))
                .perform(click())
        waitForData()
    }

    fun selectFilterStatus() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        waitForData()
    }

    fun clickFilterCategory() {
        onView(nthChildOf(withId(com.tokopedia.sortfilter.R.id.sort_filter_items),1))
                .perform(click())
        waitForData()
    }

    fun selectFilterCategory() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        waitForData()
    }

    fun clickFilterDate() {
        onView(nthChildOf(withId(com.tokopedia.sortfilter.R.id.sort_filter_items),2))
                .perform(scrollTo(), click())
        waitForData()
    }

    fun selectFilterDate() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_option))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        waitForData()
    }

    fun scrollToRecommendationList() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_order_list))
                .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(5))
        waitForData()
    }

    fun clickAtcRecommendation() {
        onView(withId(com.tokopedia.buyerorder.R.id.rv_order_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5,
                        clickOnViewChild(R.id.buttonAddToCart)))
        waitForData()
    }

    fun clickRecommendationCard() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withId(com.tokopedia.buyerorder.R.id.rv_order_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(5,
                        clickOnViewChild(com.tokopedia.buyerorder.R.id.uoh_product_item)))
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun nthChildOf(parentMatcher: Matcher<View?>, childPosition: Int): Matcher<View?> {
        return object : TypeSafeMatcher<View?>() {
            override fun describeTo(description: Description) {
                description.appendText("position $childPosition of parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View?): Boolean {
                if (view?.parent !is ViewGroup) return false
                val parent = view.parent as ViewGroup
                return (parentMatcher.matches(parent)
                        && parent.childCount > childPosition && parent.getChildAt(childPosition) == view)
            }
        }
    }

    private fun clickOnViewChild(viewId: Int) = object : ViewAction {
        override fun getConstraints() = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))
    }

    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }
}

class ResultRobot {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }
}

fun runBot(func: UohRobot.() -> Unit) = UohRobot().apply(func)