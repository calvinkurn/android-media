package com.tokopedia.wishlist

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers

class WishlistCollectionDetailRobot {

    fun loading(duration: Long = 5_000) {
        Thread.sleep(duration)
    }

    fun scrollWishlistRecyclerViewToIndex(index: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection_detail))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    fun scrollRecommendationRecyclerViewToIndex(index: Int) {
        Espresso.onView(ViewMatchers.withId(com.tokopedia.carouselproductcard.R.id.carouselProductCardRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    fun clickRecommendationRecyclerViewItem(index: Int) {
        Espresso.onView(ViewMatchers.withId(com.tokopedia.carouselproductcard.R.id.carouselProductCardRecyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.click())
            )
    }
}

fun runWishlistCollectionDetailBot(block: WishlistCollectionDetailRobot.() -> Unit) =
    WishlistCollectionDetailRobot().apply(block)
