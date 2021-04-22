package com.tokopedia.logisticaddaddress.features.district_recommendation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultData
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomContract.Constant.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS
import com.tokopedia.logisticaddaddress.test.R
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationMockHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class DiscomActivityTest {

    @get:Rule
    val activityRule =
            IntentsTestRule(DiscomActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(GET_DISTRICT_KET, InstrumentationMockHelper.getRawString(context, R.raw.district_recommendation_jakarta), MockModelConfig.FIND_BY_CONTAINS)
        }
        activityRule.launchActivity(createIntent())
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun givenValidQueryReturnsRequiredResults() {
        val testQuery = "jak"
        onView(withId(R.id.edit_text_search)).perform(typeText(testQuery), closeSoftKeyboard())

        // Bad, can't implement idling resource on baselistfragment's search delay
        Thread.sleep(DiscomFragment.DEBOUNCE_DELAY_IN_MILIS)

        onView(withId(R.id.recycler_view))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        assertThat(activityRule.activityResult, hasResultCode(Activity.RESULT_OK))
        assertThat(activityRule.activityResult,
                hasResultData(hasExtraWithKey(INTENT_DISTRICT_RECOMMENDATION_ADDRESS)))

        val discomQuery = "tracker/logistic/discom_positive.json"
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, discomQuery), hasAllSuccess())
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    private fun createIntent(): Intent {
        return Intent(InstrumentationRegistry.getInstrumentation().targetContext,
                DiscomActivity::class.java).also {
            it.data = Uri.parse(ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS)
            it.putExtra(IS_LOCALIZATION, false)
        }
    }

    companion object {
        const val GET_DISTRICT_KET = "KeroDistrictRecommendation"
        const val IS_LOCALIZATION = "is_localization"
    }
}