package com.tokopedia.wishlist.topads

import android.Manifest
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.runWishlistCollectionDetailBot
import com.tokopedia.wishlist.util.WishlistIdlingResource
import com.tokopedia.wishlist.util.adapter
import com.tokopedia.wishlist.util.setupRemoteConfig
import com.tokopedia.wishlist.view.adapter.WishlistV2Adapter
import com.tokopedia.wishlist.view.adapter.viewholder.WishlistV2RecommendationCarouselViewHolder
import com.tokopedia.wishlistcollection.view.activity.WishlistCollectionDetailActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@TopAdsTest
class WishlistCollectionDetailTopAdsVerificationTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private var topAdsAssertion = TopAdsAssertion(context) { topAdsCount }

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @get:Rule
    var activityRule =
        object :
            ActivityTestRule<WishlistCollectionDetailActivity>(WishlistCollectionDetailActivity::class.java) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
                setupRemoteConfig()
                setupTopAdsDetector()
            }
        }

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(WishlistIdlingResource.countingIdlingResource)
    }

    @After
    fun cleanup() {
        topAdsAssertion.after()
        IdlingRegistry.getInstance().unregister(WishlistIdlingResource.countingIdlingResource)
    }

    @Test
    fun testWishlistCollectionDetailTopAds() {
        runWishlistCollectionDetailBot {
            val wishlistRecyclerView =
                activityRule.activity.findViewById<RecyclerView>(R.id.rv_wishlist_collection_detail)
            val wishlistItemCount = wishlistRecyclerView?.adapter?.itemCount ?: 0
            for (wishlistIndex in 0 until wishlistItemCount) {
                scrollWishlistRecyclerViewToIndex(wishlistIndex)
                if (isRecommendationCarousel(
                        wishlistRecyclerView.findViewHolderForAdapterPosition(wishlistIndex)
                    )
                ) {
                    val recommendationItems = wishlistRecyclerView
                        .adapter<WishlistV2Adapter>()
                        .getRecommendationDataAtIndex(wishlistIndex)
                        .recommendationProductCardModelData

                    for (recommendationIndex in recommendationItems.indices) {
                        scrollRecommendationRecyclerViewToIndex(recommendationIndex)
                        if (recommendationItems[recommendationIndex].isTopAds) {
                            topAdsCount++
                            clickRecommendationRecyclerViewItem(recommendationIndex)
                        }
                    }
                }
            }

            loading()
        }
        topAdsAssertion.assert()
    }

    private fun isRecommendationCarousel(viewHolder: RecyclerView.ViewHolder?): Boolean {
        return viewHolder is WishlistV2RecommendationCarouselViewHolder
    }
}
