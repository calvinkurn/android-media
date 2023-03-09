package com.tokopedia.wishlist

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert

class WishlistCollectionRobot {

    fun loading() {
        Thread.sleep(5000)
    }

    fun clickWishlistRecyclerViewItem(index: Int) {
        onView(withId(R.id.rv_wishlist_collection))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.click())
            )
    }

    fun clickSemuaWishlist() {
        onView(withId(R.id.rv_wishlist_collection))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
    }

    fun clickCreateNewCollection() {
        Intents.intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withId(R.id.rv_wishlist_collection))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    3,
                    ViewActions.click()
                )
            )
    }

    fun clickXOnCreateNewCollectionBottomsheet() {
        Intents.intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withId(R.id.bottom_sheet_close))
            .perform(ViewActions.click())
    }

    fun clickThreeDotsMenuOnCollectionItemCard() {
        onView(withId(R.id.rv_wishlist_collection))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    MyViewAction.clickChildViewWithId(R.id.collection_kebab_menu)
                )
            )
    }

    fun submitNewCollectionName() {
        onView(withId(R.id.collection_create_name_input_text_field))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
        typeNewCollection()
    }

    fun typeNewCollection() {
        onView(withId(R.id.text_field_input))
            .perform(
                ViewActions.typeText("test new collection name"),
                ViewActions.closeSoftKeyboard()
            )
    }

    fun clickBuatKoleksiButton() {
        onView(withId(R.id.collection_create_button))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            .perform(ViewActions.click())
    }

    fun scrollWishlistRecyclerViewToIndex(index: Int) {
        onView(withId(R.id.rv_wishlist_collection))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }
    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        return ResultRobot().apply(func)
    }
}

fun runWishlistCollectionBot(block: WishlistCollectionRobot.() -> Unit) =
    WishlistCollectionRobot().apply(block)

class ResultRobot {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }
}

object MyViewAction {
    fun clickChildViewWithId(id: Int): ViewAction {
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
