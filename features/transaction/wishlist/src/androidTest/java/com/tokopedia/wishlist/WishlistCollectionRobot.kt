package com.tokopedia.wishlist

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import org.hamcrest.MatcherAssert

class WishlistCollectionRobot {

    fun loading() {
        Thread.sleep(5000)
    }

    fun clickWishlistRecyclerViewItem(index: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.click())
            )
    }

    fun createNewCollection() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        3, ViewActions.click()
                ))
    }

    fun scrollWishlistRecyclerViewToIndex(index: Int) {
        onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    fun clickFirstCollectionItem() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                                1, ViewActions.click()
                        )
                )
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
