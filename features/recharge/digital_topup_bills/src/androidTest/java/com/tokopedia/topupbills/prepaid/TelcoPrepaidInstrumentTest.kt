package com.tokopedia.topupbills.prepaid

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
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
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
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment
import com.tokopedia.topupbills.utils.CommonTelcoActions.bottomSheet_close
import com.tokopedia.topupbills.utils.CommonTelcoActions.kebabMenu_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickClearBtn
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_clickTextField
import com.tokopedia.topupbills.utils.CommonTelcoActions.promoItem_clickCopyButton
import com.tokopedia.topupbills.utils.CommonTelcoActions.promoItem_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.stubSearchNumber
import com.tokopedia.topupbills.utils.CommonTelcoActions.kebabMenu_validateContents
import com.tokopedia.topupbills.utils.CommonTelcoActions.clientNumberWidget_validateText
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateBuyWidgetDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateBuyWidgetNotDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateProductViewDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.pdp_validateViewPagerDisplayed
import com.tokopedia.topupbills.utils.CommonTelcoActions.productItemRv_scrollToPosition
import com.tokopedia.topupbills.utils.CommonTelcoActions.productItem_click
import com.tokopedia.topupbills.utils.CommonTelcoActions.productItem_clickSeeMore
import com.tokopedia.topupbills.utils.CommonTelcoActions.tabLayout_clickTabWithText
import com.tokopedia.topupbills.utils.ResourceUtils
import org.hamcrest.core.AllOf
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPrepaidInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule = ActivityTestRule(TelcoPrepaidActivity::class.java, false, false)

    @Before
    fun stubAllExternalIntents() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().toBlocking().first()
        setupGraphqlMockResponse {
            addMockResponse(KEY_QUERY_MENU_DETAIL, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN),
                MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_FAV_NUMBER, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN),
                MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_PREFIX_SELECT, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_PREFIX_SELECT),
                MockModelConfig.FIND_BY_CONTAINS)
            addMockResponse(KEY_QUERY_PRODUCT_MULTI_TAB, ResourceUtils.getJsonFromResource(PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB),
                MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, TelcoPrepaidActivity::class.java).apply {
            putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_PREPAID.toString())
            putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PULSA.toString())
            putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
            putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
        }

        LocalCacheHandler(context, DigitalTelcoPrepaidFragment.PREFERENCES_NAME).also {
            it.putBoolean(DigitalTelcoPrepaidFragment.TELCO_COACH_MARK_HAS_SHOWN, true)
            it.applyEditor()
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
    fun validate_prepaid_non_login() {
        stubSearchNumber(VALID_PHONE_NUMBER, TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL)

        Thread.sleep(2000)

        validate_show_contents_pdp_telco_not_login()
        validate_interaction_menu()
//        validate_click_on_contact_picker_and_list_fav_number()
        validate_pdp_client_number_widget_interaction()
//        click_phonebook_and_clear()
        interaction_product_not_login()
        interaction_product_filter()
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

        stubSearchNumber(
            VALID_PHONE_NUMBER,
            TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE)
        Thread.sleep(2000)
        clientNumberWidget_clickTextField()
        clientNumberWidget_validateText(VALID_PHONE_NUMBER)
    }


    /**
     * activate this test for local instrumentation test only because it contains contact picker
     */
    fun validate_click_on_contact_picker_and_list_fav_number() {
        stubContactNumber()

        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.telco_search_number_contact_picker)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(isDisplayed()))
        onView(withId(R.id.searchbar_textfield)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
        val viewInteraction = onView(withId(R.id.telco_search_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
    }

    fun validate_show_contents_pdp_telco_not_login() {
        Thread.sleep(2000)
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

        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        promoItem_clickCopyButton(viewInteraction)
        Thread.sleep(2000)
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
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input)).check(matches(withText("")))
    }

    fun interaction_product_not_login() {

        Thread.sleep(3000)
        pdp_validateViewPagerDisplayed()

        // click on product item on pulsa
        Thread.sleep(4000)
        pdp_validateProductViewDisplayed()
        val viewInteraction = onView(AllOf.allOf(isDisplayingAtLeast(30), withId(R.id.telco_product_rv))).check(matches(isDisplayed()))
        productItem_click(viewInteraction)
        pdp_validateBuyWidgetDisplayed()

        //click tab roaming
        Thread.sleep(2000)
        tabLayout_clickTabWithText("Roaming")
        productItemRv_scrollToPosition(viewInteraction, 8)
        Thread.sleep(2000)
        productItemRv_scrollToPosition(viewInteraction, 1)

        //click tab paket data, click lihat detail and close bottom sheet
        Thread.sleep(3000)
        tabLayout_clickTabWithText("Paket Data")
        pdp_validateBuyWidgetNotDisplayed()
        productItem_clickSeeMore(viewInteraction, 1)
        Thread.sleep(1000)
        onView(withId(R.id.telco_button_select_item)).check(matches(isDisplayed()))
        bottomSheet_close()

        //click product cluster on paket data
        pdp_validateBuyWidgetNotDisplayed()
        productItem_click(viewInteraction)
        pdp_validateBuyWidgetDisplayed()
    }

    fun interaction_product_filter() {
        //click tab paket data
        Thread.sleep(1000)
        tabLayout_clickTabWithText("Pulsa")
        tabLayout_clickTabWithText("Paket Data")

        onView(AllOf.allOf(isDisplayed(), withId(R.id.telco_sort_filter))).check(matches(isDisplayed()))

        //click Feature filter and choose 1 subfilter
        Thread.sleep(3000)
        onView(AllOf.allOf(withText("Feature"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper)))).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.telco_filter_rv)).check(matches(isDisplayed()))
        onView(withText(R.string.telco_reset_filter)).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(isDisplayed(), withId(R.id.telco_filter_rv))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.telco_filter_btn)).perform(click())
        onView(AllOf.allOf(isDisplayed(), withId(R.id.telco_sort_filter))).check(matches(isDisplayed()))
        Thread.sleep(5000)

        //click on Feature filter and reset subfilter
        Thread.sleep(3000)
        onView(AllOf.allOf(withText("Feature"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper)))).perform(click())
        Thread.sleep(3000)
        onView(withText(R.string.telco_reset_filter)).check(matches(isDisplayed()))
        onView(withText(R.string.telco_reset_filter)).perform(click())
        onView(withId(R.id.telco_filter_btn)).perform(click())

        //click clear cluster filter selected
        Thread.sleep(3000)
        onView(AllOf.allOf(withText("Kuota"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper)))).perform(click())
        Thread.sleep(3000)
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.telco_filter_btn)).perform(click())
        onView(withId(R.id.sort_filter_prefix)).check(matches(isDisplayed()))

        Thread.sleep(2000)
        onView(AllOf.allOf(isDisplayed(), withId(R.id.telco_sort_filter))).check(matches(isDisplayed()))
        onView(withId(R.id.sort_filter_prefix)).perform(click())
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
        private const val KEY_QUERY_PRODUCT_MULTI_TAB = "telcoProductMultiTab"

        private const val PATH_RESPONSE_PREPAID_MENU_DETAIL_LOGIN = "prepaid/response_mock_data_prepaid_menu_detail.json"
        private const val PATH_RESPONSE_PREPAID_FAV_NUMBER_LOGIN = "response_mock_data_telco_fav_number.json"
        private const val PATH_RESPONSE_PREPAID_PREFIX_SELECT = "prepaid/response_mock_data_prepaid_prefix_select.json"
        private const val PATH_RESPONSE_PREPAID_PRODUCT_MULTITAB = "prepaid/response_mock_data_prepaid_product_multitab.json"


        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY_NON_LOGIN = "tracker/recharge/recharge_telco_prepaid.json"
    }
}