package com.tokopedia.topupbills.postpaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeUp
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.utils.CommonTelcoActions.bottomSheet_close
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickClearBtn
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickContactBook
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_typeNumber
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_validateText
import com.tokopedia.topupbills.utils.CommonTelcoActions.kebabMenu_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.kebabMenu_validateContents
import com.tokopedia.topupbills.utils.CommonTelcoActions.promoItem_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.promoItem_clickCopyButton
import com.tokopedia.topupbills.utils.CommonTelcoActions.stubAccessingSavedNumber
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPostpaidInstrumentTest {

    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule(TelcoPostpaidActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
            addMockResponse(KEY_QUERY_MENU_DETAIL, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_MENU_DETAIL),
                MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_FAV_NUMBER, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_FAV_NUMBER),
                MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_PREFIX_SELECT, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_PREFIX_SELECT),
                MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_ENQUIRY, ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_ENQUIRY),
                MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, TelcoPostpaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_POSTPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PASCABAYAR.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }
        mActivityRule.launchActivity(intent)

        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_postpaid_non_login() {

        validate_show_contents_pdp_telco_not_login()
        validate_interaction_menu()
        validate_pdp_client_number_widget_interaction()
        validate_interaction_promo()
        validate_interaction_saved_number()

        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_NON_LOGIN), hasAllSuccess())
    }

    fun validate_pdp_client_number_widget_interaction() {
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER)
        Espresso.closeSoftKeyboard()
    }

    fun validate_show_contents_pdp_telco_not_login() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_page_container)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_view_pager)).check(matches(isDisplayed()))
    }

    fun validate_interaction_promo() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_validateText(EMPTY_TEXT)
        Thread.sleep(2000)

        val viewInteraction = onView(AllOf.allOf(
            AllOf.allOf(withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component),
                withParent(withId(com.tokopedia.common.topupbills.R.id.layout_widget)),
                isDisplayed()))).check(matches(isDisplayed()))

        promoItem_clickCopyButton(viewInteraction)
        Thread.sleep(2000)

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        promoItem_click(viewInteraction)
        onView(withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component)).perform(swipeUp())
        Thread.sleep(1000)
        onView(withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component)).perform(swipeUp())
        Thread.sleep(3000)
    }

    fun validate_interaction_menu() {
        kebabMenu_click()
        Thread.sleep(1000)
        kebabMenu_validateContents()
        Thread.sleep(1000)
        bottomSheet_close()
    }

    fun validate_interaction_saved_number() {
        stubAccessingSavedNumber(
            VALID_PHONE_NUMBER_2,
            TopupBillsSearchNumberFragment.InputNumberActionType.CONTACT,
            TelcoCategoryType.CATEGORY_PASCABAYAR.toString()
        )
        Thread.sleep(2000)
        clientNumberWidget_clickContactBook()
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_2)

        stubAccessingSavedNumber(
            VALID_PHONE_NUMBER_3,
            TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE,
            TelcoCategoryType.CATEGORY_PASCABAYAR.toString()
        )
        Thread.sleep(2000)
        clientNumberWidget_clickContactBook()
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_3)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val EMPTY_TEXT = ""

        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_FAV_NUMBER = "favouriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_ENQUIRY = "enquiry"

        private const val PATH_RESPONSE_POSTPAID_MENU_DETAIL = "postpaid/response_mock_data_postpaid_menu_detail.json"
        private const val PATH_RESPONSE_POSTPAID_FAV_NUMBER = "response_mock_data_telco_fav_number.json"
        private const val PATH_RESPONSE_POSTPAID_PREFIX_SELECT = "postpaid/response_mock_data_postpaid_prefix_select.json"
        private const val PATH_RESPONSE_POSTPAID_ENQUIRY = "postpaid/response_mock_data_postpaid_enquiry.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_NUMBER_2 = "085600001111"
        private const val VALID_PHONE_NUMBER_3 = "081234567890"
        private const val ANALYTIC_VALIDATOR_QUERY_NON_LOGIN = "tracker/recharge/recharge_telco_postpaid.json"
    }
}
