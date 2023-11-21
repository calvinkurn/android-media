package com.tokopedia.wishlist

import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.trackingoptimizer.repository.TrackRepository
import com.tokopedia.trackingoptimizer.sendTrack
import com.tokopedia.wishlist.collection.view.activity.WishlistCollectionActivity
import com.tokopedia.wishlist.detail.util.WishlistIdlingResource
import com.tokopedia.wishlist.util.disableWishlistCoachmark
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.tokopedia.wishlist.test.R as wishlisttestR

@CassavaTest
class WishlistCollectionCassavaTest {

    companion object {
        private const val KEY_WISHLIST_COLLECTION = "GetWishlistCollections"
        private const val KEY_CREATE_WISHLIST_COLLECTION = "CreateWishlistCollection"
        private const val KEY_GET_WISHLIST_COLLECTION_ITEMS = "GetWishlistCollectionItems"
    }

    @get:Rule
    var activityRule = IntentsTestRule(WishlistCollectionActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setupGraphqlMockResponse {
            addMockResponse(
                KEY_WISHLIST_COLLECTION,
                InstrumentationMockHelper.getRawString(
                    context,
                    wishlisttestR.raw.response_wishlist_collection
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_GET_WISHLIST_COLLECTION_ITEMS,
                InstrumentationMockHelper.getRawString(
                    context,
                    wishlisttestR.raw.response_semua_wishlist
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_CREATE_WISHLIST_COLLECTION,
                InstrumentationMockHelper.getRawString(
                    context,
                    wishlisttestR.raw.response_create_wishlist_collection
                ),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        disableWishlistCoachmark(context)
    }

    @After
    fun cleanup() {
        IdlingRegistry.getInstance().unregister(WishlistIdlingResource.countingIdlingResource)
        activityRule.finishActivity()
    }

    @Test
    fun test_wishlist_summary() {
        IdlingRegistry.getInstance().register(WishlistIdlingResource.countingIdlingResource)
        activityRule.launchActivity(null)
        // Intents.intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onIdle()

        val query = "tracker/transaction/wishlist_collection_summary.json"

        runWishlistCollectionBot {
            clickSemuaWishlist()
            loading()
            pressBackUnconditionally()
            clickCreateNewCollection()
            submitNewCollectionName()
            clickBuatKoleksiButton()
            loading()
            clickCreateNewCollection()
            clickXOnCreateNewCollectionBottomsheet()
            clickThreeDotsMenuOnCollectionItemCard()
            loading()
            pressBackUnconditionally()

            // Force TrackingQueue to send trackers
            runBlocking {
                suspendCoroutine<Any?> {
                    sendTrack(GlobalScope, TrackRepository.Companion.getInstance(context)) {
                        Timber.d("WishlistCollectionCassavaTest - finish send track")
                        it.resume(null)
                    }
                }
            }

            // Wait for TrackingQueue to finish
            Thread.sleep(1_000)
        } submit {
            hasPassedAnalytics(cassavaTestRule, query)
        }
    }
}
