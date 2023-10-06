package com.tokopedia.smartbills

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.smartbills.presentation.activity.SmartBillsAddTelcoActivity
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

class SmartBillsAddTelcoPreActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var activityRule =
            ActivityTestRule(SmartBillsAddTelcoActivity::class.java, false, false)

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
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

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, SmartBillsAddTelcoActivity::class.java).apply {
            putExtra("template", "telcopre")
            putExtra("category_id","1")
            putExtra("menu_id", "2")
        }
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateAddTelcoSmartBills(){
        closeTicker()
        clickInputField()
        clickDropDownList()
        chooseProduct()
        clickAddBillsError()
        clickBack()

        MatcherAssert.assertThat(
                cassavaTestRule.validate(SMART_BILLS_ADD_TELCO_VALIDATOR_QUERY),
                hasAllSuccess()
        )
    }

    private fun clickBack(){
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(2000)
        pressBackUnconditionally()
    }

    private fun closeTicker(){
        Thread.sleep(2000)
        onView(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.ticker_close_icon)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
        Thread.sleep(2000)
    }

    private fun clickInputField(){
        Thread.sleep(2000)
        onView(CommonMatcher.getElementFromMatchAtPosition(ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.text_field_input), 1)).
             perform(ViewActions.typeText("085327499272"))
        Thread.sleep(4000)
    }

    private fun clickDropDownList(){
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.text_field_sbm_product_nominal)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun chooseProduct(){
        Thread.sleep(2000)
        onView(ViewMatchers.withText("Rp16.500")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun clickAddBillsError(){
        Thread.sleep(2000)
        onView(ViewMatchers.withId(R.id.btn_sbm_add_telco)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val KEY_MENU_DETAIL = "rechargeCatalogMenuDetail"
        private const val KEY_PREFIX_NUMBER = "rechargeCatalogPrefixSelect"
        private const val KEY_NOMINAL_PRODUCT = "rechargeCatalogProductInputMultiTab"
        private const val KEY_ADD_BILLS = "rechargeSBMAddBill"

        private const val PATH_MENU_DETAIL = "menu_detail.json"
        private const val PATH_PREFIX_NUMBER = "prefix_number.json"
        private const val PATH_NOMINAL_PRODUCT = "nominal_product.json"
        private const val PATH_ADD_BILLS = "add_bills_errors.json"

        private const val SMART_BILLS_ADD_TELCO_VALIDATOR_QUERY = "tracker/recharge/smart_bills_management_add_telco_pre_test.json"
    }

}