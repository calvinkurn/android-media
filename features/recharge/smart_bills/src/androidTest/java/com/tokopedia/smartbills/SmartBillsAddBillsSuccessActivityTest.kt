package com.tokopedia.smartbills

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.smartbills.presentation.activity.SmartBillsActivity
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.ResourcePathUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SmartBillsAddBillsSuccessActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule =
            ActivityTestRule(SmartBillsActivity::class.java, false, false)

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_STATEMENT_MONTHS,
                    ResourcePathUtil.getJsonFromResource(PATH_STATEMENT_MONTHS),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_STATEMENT_BILLS,
                    ResourcePathUtil.getJsonFromResource(PATH_STATEMENT_BILLS),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_CATALOG_MENU,
                    ResourcePathUtil.getJsonFromResource(PATH_CATALOG_BILLS),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_MENU_DETAIL,
                    ResourcePathUtil.getJsonFromResource(PATH_MENU_DETAIL),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_PREFIX_NUMBER,
                    ResourcePathUtil.getJsonFromResource(PATH_PREFIX_NUMBER),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_NOMINAL_PRODUCT,
                    ResourcePathUtil.getJsonFromResource(PATH_NOMINAL_PRODUCT),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_ADD_BILLS,
                    ResourcePathUtil.getJsonFromResource(PATH_ADD_BILLS),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        InstrumentationAuthHelper.loginInstrumentationTestUser1()

        LocalCacheHandler(context, SmartBillsFragment.SMART_BILLS_PREF).also {
            it.putBoolean(SmartBillsFragment.SMART_BILLS_VIEWED_ONBOARDING_COACH_MARK, true)
            it.applyEditor()
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, SmartBillsActivity::class.java)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateAddSmartBillsSuccess() {
        Thread.sleep(3000)
        click_add_bills()
        click_input_field()
        click_dropdown_list()
        choose_product()
        click_add_bills_success()
        MatcherAssert.assertThat(
                cassavaTestRule.validate(SMART_BILLS_VALIDATOR_QUERY),
                hasAllSuccess()
        )
    }

    private fun click_add_bills(){
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.tv_sbm_add_bills)).perform(ViewActions.click())
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withText("Pulsa")).perform(ViewActions.click())
    }

    private fun click_input_field(){
        Thread.sleep(2000)
        Espresso.onView(CommonMatcher.getElementFromMatchAtPosition(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.text_field_input), 1)).
        perform(ViewActions.typeText("085327499272"))
        Thread.sleep(4000)
    }

    private fun click_dropdown_list(){
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.text_field_sbm_product_nominal)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun choose_product(){
        Thread.sleep(6000)
        Espresso.onView(ViewMatchers.withText("Rp16.500")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun click_add_bills_success(){
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_sbm_add_telco)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val KEY_STATEMENT_MONTHS = "rechargeStatementMonths"
        private const val KEY_STATEMENT_BILLS = "rechargeSBMList"
        private const val KEY_CATALOG_MENU = "rechargeCatalogMenu"
        private const val KEY_MENU_DETAIL = "rechargeCatalogMenuDetail"
        private const val KEY_PREFIX_NUMBER = "rechargeCatalogPrefixSelect"
        private const val KEY_NOMINAL_PRODUCT = "rechargeCatalogProductInputMultiTab"
        private const val KEY_ADD_BILLS = "rechargeSBMAddBill"

        private const val PATH_STATEMENT_MONTHS = "statement_months.json"
        private const val PATH_STATEMENT_BILLS = "statement_bills.json"
        private const val PATH_CATALOG_BILLS = "catalog_bills.json"
        private const val PATH_MENU_DETAIL = "menu_detail.json"
        private const val PATH_PREFIX_NUMBER = "prefix_number.json"
        private const val PATH_NOMINAL_PRODUCT = "nominal_product.json"
        private const val PATH_ADD_BILLS = "add_bills_success.json"


        private const val SMART_BILLS_VALIDATOR_QUERY = "tracker/recharge/smart_bills_management_add_telco_success.json"
    }
}