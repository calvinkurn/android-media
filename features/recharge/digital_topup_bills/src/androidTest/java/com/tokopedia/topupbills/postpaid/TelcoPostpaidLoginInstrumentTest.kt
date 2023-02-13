package com.tokopedia.topupbills.postpaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.utils.CommonTelcoActions
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickClearBtn
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickFilterChip_withText
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_scrollToChip_withText
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_typeNumber
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_validateText
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_clickBuyWidget
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateBuyWidgetDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateBuyWidgetNotDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.tabLayout_clickTabWithText
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPostpaidLoginInstrumentTest {

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
            addMockResponse(
                KEY_QUERY_MENU_DETAIL,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_MENU_DETAIL_LOGIN),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_QUERY_FAV_NUMBER,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_FAV_NUMBER),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_QUERY_PREFIX_SELECT,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_PREFIX_SELECT),
                MockModelConfig.FIND_BY_CONTAINS
            )
            addMockResponse(
                KEY_QUERY_ENQUIRY,
                ResourceUtils.getJsonFromResource(PATH_RESPONSE_POSTPAID_ENQUIRY),
                MockModelConfig.FIND_BY_CONTAINS
            )
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, TelcoPostpaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_POSTPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PASCABAYAR.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }
        mActivityRule.launchActivity(intent)
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validate_postpaid_login() {
        Thread.sleep(3000)

        validate_pdp_client_number_widget_interaction()
        validate_filter_chip()
        enquiry_phone_number()
        enquiry_new_input_phone_number()
        validate_interaction_saved_number()
        click_on_tab_menu_login()
        click_item_recent_widget_login()

        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_LOGIN), hasAllSuccess())
    }

    fun validate_pdp_client_number_widget_interaction() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER)
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER)

        // validate autocomplete
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(PREFIX_PHONE_NUMBER)
        Thread.sleep(2000)
        onView(withText("Danur Kebab 2"))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(matches(isDisplayed()))
            .perform(click())

        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(PREFIX_PHONE_NUMBER)
        Thread.sleep(2000)
        onView(withText("081208120812"))
            .inRoot(RootMatchers.isPlatformPopup())
            .check(matches(isDisplayed()))
            .perform(click())

        clientNumberWidget_clickClearBtn()
    }

    fun validate_filter_chip() {
        clientNumberWidget_clickFilterChip_withText("Tokopedia")
        clientNumberWidget_validateText("081232323239")
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_scrollToChip_withText("081208120812")
        clientNumberWidget_clickFilterChip_withText("081208120812")
        clientNumberWidget_validateText("081208120812")
    }

    fun click_on_tab_menu_login() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_validateText(EMPTY_TEXT)
        Thread.sleep(3000)
        tabLayout_clickTabWithText("Transaksi Terakhir")
        Thread.sleep(3000)
        tabLayout_clickTabWithText("Promo")
    }

    fun enquiry_phone_number() {
        Thread.sleep(2000)
        clientNumberWidget_clickEnquiryButton()
        Thread.sleep(1000)
        clientNumberWidget_validateEnquiryButtonNotDisplayed()
        clientNumberWidget_validateEnquiryResultDisplayed()
        closeSoftKeyboard()
        pdp_validateBuyWidgetDisplayed()
        pdp_clickBuyWidget()
    }

    fun enquiry_new_input_phone_number() {
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_typeNumber(VALID_PHONE_NUMBER_2)
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_2)

        Thread.sleep(2000)
        pdp_validateBuyWidgetNotDisplayed()
        clientNumberWidget_clickEnquiryButton()

        Thread.sleep(2000)
        clientNumberWidget_validateEnquiryButtonNotDisplayed()
        clientNumberWidget_validateEnquiryResultDisplayed()
        closeSoftKeyboard()
        pdp_validateBuyWidgetDisplayed()
        pdp_clickBuyWidget()
    }

    fun validate_interaction_saved_number() {
        CommonTelcoActions.stubAccessingSavedNumber(
            VALID_PHONE_NUMBER_2,
            TopupBillsSearchNumberFragment.InputNumberActionType.CONTACT,
            TelcoCategoryType.CATEGORY_PASCABAYAR.toString()
        )
        Thread.sleep(2000)
        CommonTelcoActions.clientNumberWidget_clickContactBook()
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_2)

        CommonTelcoActions.stubAccessingSavedNumber(
            VALID_PHONE_NUMBER_3,
            TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE,
            TelcoCategoryType.CATEGORY_PASCABAYAR.toString()
        )
        Thread.sleep(2000)
        CommonTelcoActions.clientNumberWidget_clickContactBook()
        Thread.sleep(2000)
        clientNumberWidget_validateText(VALID_PHONE_NUMBER_3)
    }

    fun click_item_recent_widget_login() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Thread.sleep(3000)
        tabLayout_clickTabWithText("Transaksi Terakhir")
        val viewInteraction = onView(
            AllOf.allOf(
                isDescendantOfA(withId(R.id.layout_widget)),
                withId(R.id.recycler_view_menu_component),
                isDisplayed()
            )
        ).check(matches(isDisplayed()))
        viewInteraction.perform(
            RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(0, click())
        )
        Thread.sleep(3000)
        enquiry_phone_number()
    }

    private fun clientNumberWidget_clickEnquiryButton() {
        onView(withId(R.id.telco_enquiry_btn))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    private fun clientNumberWidget_validateEnquiryButtonNotDisplayed() {
        onView(withId(R.id.telco_enquiry_btn)).check(matches(IsNot.not(isDisplayed())))
    }

    private fun clientNumberWidget_validateEnquiryResultDisplayed() {
        onView(withId(R.id.telco_title_enquiry_result)).check(matches(isDisplayed()))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val EMPTY_TEXT = ""
        private const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        private const val KEY_QUERY_FAV_NUMBER = "rechargeFetchFavoriteNumber"
        private const val KEY_QUERY_PREFIX_SELECT = "telcoPrefixSelect"
        private const val KEY_QUERY_ENQUIRY = "enquiry"

        private const val PATH_RESPONSE_POSTPAID_MENU_DETAIL_LOGIN = "postpaid/response_mock_data_postpaid_menu_detail_login.json"
        private const val PATH_RESPONSE_POSTPAID_FAV_NUMBER = "postpaid/response_mock_data_postpaid_fav_number_login.json"
        private const val PATH_RESPONSE_POSTPAID_PREFIX_SELECT = "postpaid/response_mock_data_postpaid_prefix_select.json"
        private const val PATH_RESPONSE_POSTPAID_ENQUIRY = "postpaid/response_mock_data_postpaid_enquiry.json"

        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_NUMBER_2 = "085252525252"
        private const val VALID_PHONE_NUMBER_3 = "081234567890"
        private const val PREFIX_PHONE_NUMBER = "0812"
        private const val ANALYTIC_VALIDATOR_QUERY_LOGIN = "tracker/recharge/recharge_telco_postpaid_login.json"
    }
}
