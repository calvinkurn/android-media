package com.tokopedia.wishlist.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.wishlist.R
import com.tokopedia.wishlist.detail.util.WishlistIdlingResource
import com.tokopedia.wishlist.detail.view.activity.WishlistCollectionDetailActivity
import com.tokopedia.wishlist.detail.view.adapter.WishlistAdapter
import com.tokopedia.wishlist.detail.view.adapter.viewholder.WishlistRecommendationCarouselViewHolder
import com.tokopedia.wishlist.runWishlistCollectionDetailBot
import com.tokopedia.wishlist.util.adapter
import com.tokopedia.wishlist.util.setupRemoteConfig
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.wishlist.test.R as wishlisttestR

@TopAdsTest
class WishlistCollectionDetailTopAdsVerificationTest {

    companion object {
        private const val KEY_TOPADS_BANNER = "topadsDisplayBannerAdsV3"
    }

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
                setupGraphqlMockResponse {
                    addMockResponse(
                        KEY_TOPADS_BANNER,
                        InstrumentationMockHelper.getRawString(
                            context,
                            wishlisttestR.raw.response_topads_banner
                        ),
                        MockModelConfig.FIND_BY_CONTAINS
                    )
                }
            }
        }

    @Before
    fun setup() {
        Intents.init()
        IdlingRegistry.getInstance().register(WishlistIdlingResource.countingIdlingResource)
    }

    @After
    fun cleanup() {
        topAdsAssertion.after()
        IdlingRegistry.getInstance().unregister(WishlistIdlingResource.countingIdlingResource)
        Intents.release()
    }

    @Test
    fun testWishlistCollectionDetailTopAds() {
        runWishlistCollectionDetailBot {
            loading(5000)
            Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
            val wishlistRecyclerView =
                activityRule.activity.findViewById<RecyclerView>(R.id.rv_wishlist_collection_detail)
            val wishlistItemCount = wishlistRecyclerView?.adapter?.itemCount ?: 0
            loading(5000)
            for (wishlistIndex in 0 until wishlistItemCount) {
                scrollWishlistRecyclerViewToIndex(wishlistIndex)
                if (isRecommendationCarousel(
                        wishlistRecyclerView.findViewHolderForAdapterPosition(wishlistIndex)
                    )
                ) {
                    val recommendationItems = wishlistRecyclerView
                        .adapter<WishlistAdapter>()
                        .getRecommendationDataAtIndex(wishlistIndex)
                        .recommendationProductCardModelData

                    // root cause failed issue : pending in topads banner (which is topads productName is empty)
                    for (recommendationIndex in recommendationItems.indices) {
                        scrollRecommendationRecyclerViewToIndex(recommendationIndex)
                        if (recommendationItems[recommendationIndex].isTopAds) {
                            topAdsCount++
                            clickRecommendationRecyclerViewItem(recommendationIndex)
                        }
                    }
                }
            }
            loading(5000)
        }
        topAdsAssertion.assert()
    }

    private fun isRecommendationCarousel(viewHolder: RecyclerView.ViewHolder?): Boolean {
        return viewHolder is WishlistRecommendationCarouselViewHolder
    }
}
