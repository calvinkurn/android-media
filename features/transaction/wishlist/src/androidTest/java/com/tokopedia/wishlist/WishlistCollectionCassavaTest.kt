package com.tokopedia.wishlist

import android.app.Activity
import android.app.Instrumentation
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.test.application.util.setupTopAdsDetector
import com.tokopedia.trackingoptimizer.repository.TrackRepository
import com.tokopedia.trackingoptimizer.sendTrack
import com.tokopedia.wishlist.util.WishlistIdlingResource
import com.tokopedia.wishlistcollection.view.activity.WishlistCollectionActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.tokopedia.wishlist.test.R
import com.tokopedia.wishlist.util.adapter
import com.tokopedia.wishlist.util.disableWishlistCoachmark
import com.tokopedia.wishlist.util.setupRemoteConfig
import com.tokopedia.wishlistcollection.view.adapter.WishlistCollectionAdapter
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionCreateItemViewHolder
import com.tokopedia.wishlistcollection.view.adapter.viewholder.WishlistCollectionRecommendationItemViewHolder

@CassavaTest
class WishlistCollectionCassavaTest {

    companion object {
        private const val KEY_WISHLIST_COLLECTION = "GetWishlistCollections"
    }

    @get:Rule
    var activityRule =
            object :
                    ActivityTestRule<WishlistCollectionActivity>(WishlistCollectionActivity::class.java) {
                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()
                    setupGraphqlMockResponse {
                        addMockResponse(
                                KEY_WISHLIST_COLLECTION,
                                InstrumentationMockHelper.getRawString(
                                        context,
                                        R.raw.response_wishlist_collection
                                ),
                                MockModelConfig.FIND_BY_CONTAINS
                        )
                    }
                    InstrumentationAuthHelper.loginInstrumentationTestUser1()
                    disableWishlistCoachmark(context)
                }
            }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    /*@Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_WISHLIST_COLLECTION,
                    InstrumentationMockHelper.getRawString(
                            context,
                            R.raw.response_wishlist_collection
                    ),
                    MockModelConfig.FIND_BY_CONTAINS
            )
        }
    }*/

   /* @Before
    fun setup() {
        IdlingRegistry.getInstance().register(WishlistIdlingResource.countingIdlingResource)
    }*/

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(WishlistIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test_wishlist_summary() {
        // dari sini sampe onIdle() ga ada
        IdlingRegistry.getInstance().register(WishlistIdlingResource.countingIdlingResource)
        activityRule.launchActivity(null)
        Intents.intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onIdle()

        runWishlistCollectionBot {
            val wishlistCollectionRecyclerView =
                    activityRule.activity.findViewById<RecyclerView>(com.tokopedia.wishlist.R.id.rv_wishlist_collection)
            createNewCollection()

            /*val itemCount = wishlistCollectionRecyclerView.adapter?.itemCount ?: 0
            for (index in 0 until itemCount) {
                // scrollWishlistRecyclerViewToIndex(index)
                if (isCreateNewItem(
                                wishlistCollectionRecyclerView.findViewHolderForAdapterPosition(index)
                        )
                ) {
                    clickWishlistRecyclerViewItem(index)
                }
            }*/

            loading()
        }
        /*activityRule.launchActivity(null)
        Intents.intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onIdle()

        // val query = "tracker/transaction/uoh_summary.json"

        runWishlistCollectionBot {
            {
                *//*loading()
                clickFirstCollectionItem()*//*
                loading()
                createNewCollection()
                loading()
            }
        }*/
    }

    private fun isCreateNewItem(viewHolder: RecyclerView.ViewHolder?): Boolean {
        return viewHolder is WishlistCollectionCreateItemViewHolder
    }
}
