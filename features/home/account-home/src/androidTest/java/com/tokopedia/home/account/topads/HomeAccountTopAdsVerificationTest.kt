package com.tokopedia.home.account.topads

import android.Manifest
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.home.account.R
import com.tokopedia.home.account.environment.InstrumentationHomeAccountTestActivity
import com.tokopedia.home.account.presentation.viewholder.RecommendationProductViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeAccountTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule = object : ActivityTestRule<InstrumentationHomeAccountTestActivity>(InstrumentationHomeAccountTestActivity::class.java) {
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
    fun testTopAdsHomeAccount() {
        waitForData()

        val buyerRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_buyer)
        val itemCount = buyerRecyclerView.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            scrollHomeAccountRecyclerViewToPosition(buyerRecyclerView, i)
            checkProductOnDynamicChannel(buyerRecyclerView, i)
        }

        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(buyerRecyclerView: RecyclerView, i: Int) {
        when (buyerRecyclerView.findViewHolderForAdapterPosition(i)) {
            is RecommendationProductViewHolder -> {
                waitForData()
                clickOnEachItemRecyclerView(
                        activityRule.activity.findViewById(com.tokopedia.home.account.test.R.id.container_home_account),
                        buyerRecyclerView.id,
                        0
                )
            }
        }
    }

    private fun scrollHomeAccountRecyclerViewToPosition(buyerRecyclerView: RecyclerView, position: Int) {
        val layoutManager = buyerRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}