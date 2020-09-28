package com.tokopedia.officialstore.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestActivity
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector

import org.junit.*

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelMixLeftViewHolder
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelMixTopViewHolder

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for OSPage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class OSTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationOfficialStoreTestActivity>(InstrumentationOfficialStoreTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Before
    fun setTopAdsAssertion() {
        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsHome() {
        waitForData()
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemAdapter: OfficialHomeAdapter = recyclerView.adapter as OfficialHomeAdapter

        Espresso.onView(withId(R.id.recycler_view)).perform(ViewActions.swipeUp())

        waitForData()

        val itemCount = itemAdapter.getVisitables().size
        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MixTopComponentViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is MixLeftComponentViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_product, 0)
            }
            is DynamicChannelMixLeftViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_product, 0)
            }
            is DynamicChannelMixTopViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is OfficialProductRecommendationViewHolder -> {
                Espresso.onView(withId(R.id.recycler_view))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }
}
