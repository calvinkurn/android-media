package com.tokopedia.navigation.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.navigation.R
import com.tokopedia.navigation.environment.InstrumentationInboxTestActivity
import com.tokopedia.navigation.presentation.adapter.viewholder.RecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxTopAdsBannerViewHolder
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InboxTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationInboxTestActivity>(InstrumentationInboxTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
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
    fun setTopAdsAssertion() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )

        login()
        waitForData()
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    @Test
    fun testTopAdsInbox() {
        waitForData()

        val inboxRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recyclerview)
        val itemCount = inboxRecyclerView.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            scrollHomeAccountRecyclerViewToPosition(inboxRecyclerView, i)
            checkProductOnDynamicChannel(inboxRecyclerView, i)
        }

        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(inboxRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = inboxRecyclerView.findViewHolderForAdapterPosition(i)) {
            is RecommendationViewHolder -> {
                activityRule.runOnUiThread { viewHolder.itemView.findViewById<View>(R.id.productCardView).performClick() }
            }
            is InboxTopAdsBannerViewHolder -> {
                activityRule.runOnUiThread { viewHolder.itemView.findViewById<View>(R.id.topads_banner).performClick() }
            }
        }
    }

    private fun scrollHomeAccountRecyclerViewToPosition(inboxRecyclerView: RecyclerView, position: Int) {
        val layoutManager = inboxRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}