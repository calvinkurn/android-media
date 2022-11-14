package com.tokopedia.wishlist

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import timber.log.Timber

class WishlistCollectionRobot {

    fun loading(duration: Long = 5_000) {
        Thread.sleep(duration)
    }

    fun hideCoachmark() {
        runCatching {
            Espresso.onView(ViewMatchers.withId(com.tokopedia.coachmark.R.id.step_ic_close))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(ViewActions.click())
        }.onFailure {
            Timber.e(it)
        }
    }

    fun clickWishlistRecyclerViewItem(index: Int) {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
                .perform(
                    RecyclerViewActions
                        .actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.click())
                )
        } catch (e: PerformException) {
            Timber.e(e)
        }
    }

    fun scrollWishlistRecyclerViewToIndex(index: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }
}

fun runWishlistCollectionBot(block: WishlistCollectionRobot.() -> Unit) =
    WishlistCollectionRobot().apply(block)
