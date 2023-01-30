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
import com.tokopedia.wishlist.runWishlistCollectionBot
import com.tokopedia.wishlist.util.WishlistIdlingResource
import com.tokopedia.wishlist.util.adapter
import com.tokopedia.wishlist.util.disableWishlistCoachmark
import com.tokopedia.wishlist.util.setupRemoteConfig
import com.tokopedia.wishlistcollection.view.activity.WishlistCollectionActivity
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionRecommendationItemViewHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@TopAdsTest
class WishlistCollectionTopAdsVerificationTest {

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
            ActivityTestRule<WishlistCollectionActivity>(WishlistCollectionActivity::class.java) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
                setupRemoteConfig()
                setupTopAdsDetector()
                disableWishlistCoachmark(context)
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
    fun testWishlistCollectionTopAds() {
        runWishlistCollectionBot {
            val wishlistCollectionRecyclerView =
                activityRule.activity.findViewById<RecyclerView>(R.id.rv_wishlist_collection)
            val itemCount = wishlistCollectionRecyclerView.adapter?.itemCount ?: 0
            for (index in 0 until itemCount) {
                scrollWishlistRecyclerViewToIndex(index)
                if (isRecommendationItem(
                        wishlistCollectionRecyclerView.findViewHolderForAdapterPosition(index)
                    )
                ) {
                    val recommendationItem = wishlistCollectionRecyclerView
                        .adapter<WishlistCollectionAdapter>()
                        .getRecommendationItemAtIndex(index)
                    if (recommendationItem.isTopAds) {
                        topAdsCount++
                        clickWishlistRecyclerViewItem(index)
                    }
                }
            }

            loading()
        }
        topAdsAssertion.assert()
    }

    private fun isRecommendationItem(viewHolder: RecyclerView.ViewHolder?): Boolean {
        return viewHolder is WishlistCollectionRecommendationItemViewHolder
    }
}
