package com.tokopedia.favorite

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
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
    var activityRule = object : IntentsTestRule<InstrumentationFavoriteTestActivity>(
            InstrumentationFavoriteTestActivity::class.java
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            login()
            setupTopAdsDetector()
        }
    }

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @Before
    fun doBeforeRun() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
    }

    @After
    fun doAfterRun() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAds() {
        waitForData(5)

        val outerRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.index_favorite_recycler_view)
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

        topAdsAssertion?.assert()
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, pixelToScroll: Int) {
        activityRule.runOnUiThread { recyclerView.scrollBy(0, pixelToScroll) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    private fun waitForData(delayInSeconds: Int) {
        Thread.sleep(delayInSeconds * 1000L)
    }

}
