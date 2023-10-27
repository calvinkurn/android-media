package com.tokopedia.wishlist

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.carouselproductcard.R as carouselproductcardR

class WishlistCollectionDetailRobot {

    fun loading(duration: Long = 180000) {
        Thread.sleep(duration)
    }

    fun scrollWishlistRecyclerViewToIndex(index: Int) {
        Espresso.onView(ViewMatchers.withId(R.id.rv_wishlist_collection_detail))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    fun scrollRecommendationRecyclerViewToIndex(index: Int) {
        Espresso.onView(ViewMatchers.withId(carouselproductcardR.id.carouselProductCardRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(index))
    }

    fun clickRecommendationRecyclerViewItem(index: Int) {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Espresso.onView(ViewMatchers.withId(carouselproductcardR.id.carouselProductCardRecyclerView))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(index, ViewActions.click())
            )
    }
}

fun runWishlistCollectionDetailBot(block: WishlistCollectionDetailRobot.() -> Unit) =
    WishlistCollectionDetailRobot().apply(block)
