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
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.containsMapOf
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS
import com.tokopedia.logisticaddaddress.interceptor.AddAddressInterceptor
import com.tokopedia.logisticaddaddress.utils.SimpleIdlingResource
import com.tokopedia.test.application.util.InstrumentationMockHelper.getRawString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.tokopedia.logisticaddaddress.R as logisticaddaddressR
import com.tokopedia.logisticaddaddress.test.R as logisticaddaddresstestR
import com.tokopedia.unifycomponents.R as unifycomponentsR

@RunWith(AndroidJUnit4::class)
@MediumTest
class DiscomActivityTest {

    @get:Rule
    val activityRule =
        IntentsTestRule(DiscomActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private val logisticInterceptor = AddAddressInterceptor.logisticInterceptor

    @Before
    fun setup() {
        AddAddressInterceptor.resetAllCustomResponse()
        AddAddressInterceptor.setupGraphqlMockResponse(context)
        logisticInterceptor.getDistrictRecommendationResponsePath = getRawString(context, logisticaddaddresstestR.raw.district_recommendation_jakarta)
        activityRule.launchActivity(createIntent())
        IdlingRegistry.getInstance().register(SimpleIdlingResource.countingIdlingResource)
    }

    @Test
    fun givenValidQueryReturnsRequiredResults() {
        val testQuery = "jak"
        onView(withId(logisticaddaddressR.id.search_page_input)).perform(click())
        onView(withId(unifycomponentsR.id.searchbar_textfield))
            .perform(click(), replaceText(testQuery), closeSoftKeyboard())
        Thread.sleep(1000L)

        onView(withId(logisticaddaddressR.id.rv_list_district))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        assertThat(activityRule.activityResult, hasResultCode(Activity.RESULT_OK))
        assertThat(
            activityRule.activityResult,
            hasResultData(hasExtraWithKey(INTENT_DISTRICT_RECOMMENDATION_ADDRESS))
        )

        val query = mapOf(
            "event" to "clickShipping",
            "eventCategory" to "cart change address",
            "eventAction" to "click checklist kota atau kecamatan pada \\+ address",
            "eventLabel" to ".*"
        )
        assertThat(cassavaTestRule.getRecent(), containsMapOf(query, CassavaTestRule.MODE_SUBSET))
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(SimpleIdlingResource.countingIdlingResource)
    }

    private fun createIntent(): Intent {
        return Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            DiscomActivity::class.java
        ).also {
            it.data = Uri.parse(ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS)
            it.putExtra(IS_LOCALIZATION, false)
        }
    }

    companion object {
        const val IS_LOCALIZATION = "is_localization"
    }
}
