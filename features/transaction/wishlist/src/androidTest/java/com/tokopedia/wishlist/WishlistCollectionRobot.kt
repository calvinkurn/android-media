package com.tokopedia.wishlist

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers

class WishlistCollectionRobot {

    fun loading(duration: Long = 5_000) {
        Thread.sleep(duration)
    }

    fun clickWishlistRecyclerViewItem(index: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.click())
            )
    }

    fun scrollWishlistRecyclerViewToIndex(index: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }
}

fun runWishlistCollectionBot(block: WishlistCollectionRobot.() -> Unit) =
    WishlistCollectionRobot().apply(block)
