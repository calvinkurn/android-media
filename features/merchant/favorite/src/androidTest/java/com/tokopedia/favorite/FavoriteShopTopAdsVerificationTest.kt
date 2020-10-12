package com.tokopedia.favorite

import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.favorite.view.FavoriteShopsActivity
import com.tokopedia.favorite.view.adapter.TopAdsShopAdapter
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteShopTopAdsVerificationTest {

    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object : IntentsTestRule<FavoriteShopsActivity>(
            FavoriteShopsActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
        }
    }

    @Before
    fun doBeforeRun() {
        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
        Intents.intending(IntentMatchers.anyIntent())
                .respondWith(Instrumentation.ActivityResult(Activity.RESULT_CANCELED, null))
    }

    @After
    fun doAfterRun() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAds() {
        login()
        waitForData(20)

        /*val outerRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.index_favorite_recycler_view)
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rec_shop_recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        for (position in 0 until itemCount) {
            if (position != 0) {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as TopAdsShopAdapter.ViewHolder
                scrollRecyclerViewToPosition(outerRecyclerView, viewHolder.mainContent.height)
            }
            waitForData(5)
            onView(allOf(
                    withId(R.id.main_content),
                    withContentDescription(TopAdsShopAdapter.mainContentDescription(position))
            )).perform(ViewActions.click())
        }

        topAdsAssertion?.assert()*/
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, pixelToScroll: Int) {
        activityRule.runOnUiThread { recyclerView.scrollBy(0, pixelToScroll) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun waitForData(delayInSeconds: Int) {
        Thread.sleep(delayInSeconds * 1000L)
    }

}
