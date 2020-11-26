package com.tokopedia.home_recom.topads

import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.activity.HomeRecommendationActivityTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 26/07/20.
 */

class HomeRecomTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

    @get:Rule
    var activityRule: ActivityTestRule<HomeRecommendationActivityTest> = ActivityTestRule(HomeRecommendationActivityTest::class.java)

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

        clickOnEachItemRecyclerView(activityRule.activity.findViewById(com.tokopedia.home_recom.test.R.id.container_home), recyclerView.id, 0)
        topAdsAssertion?.assert()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }
}
