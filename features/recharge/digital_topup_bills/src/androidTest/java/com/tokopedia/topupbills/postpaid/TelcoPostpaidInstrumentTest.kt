package com.tokopedia.topupbills.postpaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.ContactsContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.TelcoContactHelper
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.utils.CommonTelcoActions.bottomSheet_close
import com.tokopedia.topupbills.utils.CommonTelcoActions.kebabMenu_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickClearBtn
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickTextField
import com.tokopedia.topupbills.utils.CommonTelcoActions.promoItem_clickCopyButton
import com.tokopedia.topupbills.utils.CommonTelcoActions.stubSearchNumber
import com.tokopedia.topupbills.utils.CommonTelcoActions.kebabMenu_validateContents
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_validateText
import com.tokopedia.topupbills.utils.CommonTelcoActions.promoItem_click
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPostpaidInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule(TelcoPostpaidActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().toBlocking().first()
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

    private fun stubContactNumber() {
        val telcoContactHelper = TelcoContactHelper()
        val contentResolver = mActivityRule.activity.contentResolver

        Intents.intending(AllOf.allOf(IntentMatchers.hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
            IntentMatchers.hasAction(Intent.ACTION_PICK))
        ).respondWith(telcoContactHelper.createUriContact(contentResolver))
    }

    @Test
    fun validate_postpaid_non_login() {

        validate_show_contents_pdp_telco_not_login()
        validate_interaction_menu()
        validate_pdp_client_number_widget_interaction()
//        validate_click_on_contact_picker_and_list_fav_number()
//        click_phonebook_and_clear()
        validate_interaction_promo()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_NON_LOGIN),
            hasAllSuccess())
    }

    fun validate_pdp_client_number_widget_interaction() {
        stubSearchNumber(
            VALID_PHONE_NUMBER,
            TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL)
        Thread.sleep(2000)
        clientNumberWidget_clickTextField()
        clientNumberWidget_validateText(VALID_PHONE_NUMBER)
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

        val viewInteraction = onView(AllOf.allOf(
            AllOf.allOf(withId(R.id.recycler_view_menu_component),
                withParent(withId(R.id.layout_widget)),
                isDisplayed()))).check(matches(isDisplayed()))

        promoItem_clickCopyButton(viewInteraction)
        Thread.sleep(2000)

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        promoItem_click(viewInteraction)
        Thread.sleep(3000)
    }

    fun validate_interaction_menu() {
        kebabMenu_click()
        Thread.sleep(1000)
        kebabMenu_validateContents()
        Thread.sleep(1000)
        bottomSheet_close()
    }

    private fun pick_phone_number_from_phonebook() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_contact_picker_btn)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).check(matches(isDisplayed()))
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
    }

    /**
     * activate this test for local instrumentation test only because it contains contact picker
     */
    fun click_phonebook_and_clear() {
        stubContactNumber()

        Thread.sleep(2000)
        pick_phone_number_from_phonebook()
        clientNumberWidget_clickClearBtn()
        clientNumberWidget_validateText(EMPTY_TEXT)
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
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY_NON_LOGIN = "tracker/recharge/recharge_telco_postpaid.json"
    }
}