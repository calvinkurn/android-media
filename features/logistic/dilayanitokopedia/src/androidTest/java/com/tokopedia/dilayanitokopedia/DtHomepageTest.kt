package com.tokopedia.dilayanitokopedia

import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.dilayanitokopedia.test.R
import com.tokopedia.dilayanitokopedia.ui.home.DtHomeActivity
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@CassavaTest
@RunWith(AndroidJUnit4::class)
class DtHomepageTest {

    companion object {
        private const val QUERY_ID = "752"
    }

    @get:Rule
    var cassavaRule = CassavaTestRule(isFromNetwork = true, sendValidationResult = false)

    private val applicationContext: Context
        get() = InstrumentationRegistry
            .getInstrumentation().context.applicationContext

    @get:Rule
    var mActivityTestRule = object : IntentsTestRule<DtHomeActivity>(DtHomeActivity::class.java, false, false) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            initMockResponse()
        }
    }

    private fun initMockResponse() {
        setupGraphqlMockResponse {
            addMockResponse(
                "getHomeIconV2",
                InstrumentationMockHelper.getRawString(applicationContext, R.raw.anchortab_dt_homepage_response_mock),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                "getHomeChannelV2",
                InstrumentationMockHelper.getRawString(applicationContext, R.raw.channel_dt_homepage_response_mock),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                "getHomeRecommendationProductV2",
                InstrumentationMockHelper.getRawString(applicationContext, R.raw.recommendation_dt_homepage_response_mock),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }
    }

    @Test
    fun productCard() {
        dtHomepage {
            launch(mActivityTestRule)
            impressFirstWidget()
            clickFirstItemInFirstWidget()
        } validateAnalytics {
            hasPassedAnalytics(cassavaRule, QUERY_ID)
        }
    }
}
