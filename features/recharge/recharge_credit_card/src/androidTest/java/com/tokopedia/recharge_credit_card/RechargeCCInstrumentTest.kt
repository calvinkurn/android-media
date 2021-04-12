
package com.tokopedia.recharge_credit_card

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.recharge_credit_card.utils.ResourceUtils
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RechargeCCInstrumentTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule = ActivityTestRule(RechargeCCActivity::class.java, false, false)

    @Before
    fun setUp() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_QUERY_BANK_LIST,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_BANK_LIST),
                    MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                    KEY_QUERY_MENU_DETAIL,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL),
                    MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                    KEY_QUERY_PREFIXES,
                    ResourceUtils.getJsonFromResource(PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES),
                    MockModelConfig.FIND_BY_CONTAINS
            )
        }
        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, RechargeCCActivity::class.java)
        activityRule.launchActivity(intent)
    }

    private fun typeCreditCardNumber(ccNumber: String) {
        onView(allOf(supportsInputMethods(), isDescendantOfA(withId(R.id.cc_widget_client_number))))
                .perform(typeText(ccNumber))
    }

    private fun openConfirmationDialogThenClickNext() {
        onView(withId(R.id.cc_button_next)).perform(click())
        onView(withText(R.string.cc_title_dialog))
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(allOf(withText(R.string.cc_cta_btn_primary), isDisplayed()))
                .perform(click())
    }

    private fun openConfirmationDialogThenClickBack() {
        onView(withId(R.id.cc_button_next)).perform(click())
        onView(withText(R.string.cc_title_dialog))
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(allOf(withText(R.string.cc_cta_btn_secondary), isDisplayed()))
                .perform(click())
    }

    private fun openBankListBottomSheetThenClose() {
        onView(withId(R.id.list_bank_btn)).perform(click())
        onView(withId(R.id.desc_bank_list)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    @Test
    fun next_button_is_enabled_on_inserted_valid_credit_card_number() {
        onView(withId(R.id.cc_button_next)).check(matches(not(isEnabled())))
        typeCreditCardNumber(VALID_CC_NUMBER)
        Thread.sleep(500)
        onView(withId(R.id.cc_button_next)).check(matches(isEnabled()))
        Thread.sleep(2000)
    }


    @Test
    fun next_button_is_disabled_on_inserted_invalid_credit_card_number() {
        onView(withId(R.id.cc_button_next)).check(matches(not(isEnabled())))
        typeCreditCardNumber(INVALID_CC_NUMBER)
        Thread.sleep(500)
        onView(withId(R.id.cc_button_next)).check(matches(not(isEnabled())))
        onView(withText(R.string.cc_error_invalid_number)).check(matches(isDisplayed()))
        Thread.sleep(2000)
    }

    @Test
    fun validate_credit_card_user_journey() {
        typeCreditCardNumber(VALID_CC_NUMBER)
        openBankListBottomSheetThenClose()
        openConfirmationDialogThenClickBack()

        openConfirmationDialogThenClickNext()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY), hasAllSuccess())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_credit_card.json"
        private const val VALID_CC_NUMBER = "4111111111111111"
        private const val INVALID_CC_NUMBER = "4141414141414141"

        private const val KEY_QUERY_BANK_LIST = "rechargeBankList"
        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_PREFIXES = "catalogPrefix"

        private const val PATH_RESPONSE_RECHARGE_BANK_LIST = "response_mock_data_cc_bank_list.json"
        private const val PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL = "response_mock_data_cc_menu_detail.json"
        private const val PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES = "response_mock_data_cc_prefixes.json"
    }
}