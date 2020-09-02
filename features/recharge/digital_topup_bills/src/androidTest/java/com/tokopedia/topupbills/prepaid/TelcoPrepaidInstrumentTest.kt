package com.tokopedia.topupbills.prepaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.ContactsContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.TelcoContactHelper
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment
import org.hamcrest.core.AllOf
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPrepaidInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule: IntentsTestRule<TelcoPrepaidActivity> = object : IntentsTestRule<TelcoPrepaidActivity>(TelcoPrepaidActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, TelcoPrepaidActivity::class.java).apply {
                putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_PREPAID.toString())
                putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PULSA.toString())
                putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
                putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()

            setupGraphqlMockResponseWithCheck(TelcoPrepaidMockResponseConfig())
        }
    }

    @Before
    fun stubAllExternalIntents() {
        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsFavNumberItem(clientNumber = VALID_PHONE_NUMBER)
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
                TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun stubSearchNumber() {
        Intents.intending(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasShortClassName(".DigitalSearchNumberActivity")))
                .respondWith(createOrderNumberTypeManual())
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
        stubSearchNumber()

        validate_coachmark()
        validate_show_contents_pdp_telco_not_login()
        validate_interaction_menu()
//        validate_click_on_contact_picker_and_list_fav_number()
        validate_click_done_keyboard_fav_number()
        choose_fav_number_from_list_fav_number()
//        click_phonebook_and_clear()
        interaction_product_not_login()
        interaction_product_filter()
        validate_interaction_promo()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_NON_LOGIN),
                hasAllSuccess())
    }

    fun validate_coachmark() {
        Thread.sleep(4000)
        val localCacheHandler = LocalCacheHandler(context, DigitalTelcoPrepaidFragment.PREFERENCES_NAME)
        if (!localCacheHandler.getBoolean(DigitalTelcoPrepaidFragment.TELCO_COACH_MARK_HAS_SHOWN, false)) {
            onView(withText(R.string.Telco_title_showcase_client_number)).check(matches(isDisplayed()))
            onView(withText(R.id.text_next)).perform(click())
            onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
            onView(withText(R.id.text_previous)).perform(click())
            onView(withText(R.string.Telco_title_showcase_client_number)).check(matches(isDisplayed()))
            onView(withText(R.id.text_next)).perform(click())
            onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
            onView(withText(R.id.text_next)).perform(click())
        }
    }

    fun validate_click_done_keyboard_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.searchbar_textfield)).perform(typeText(VALID_PHONE_NUMBER), pressImeActionButton())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
        Thread.sleep(1000)
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText("")))
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
        onView(withId(R.id.telco_ac_input_number)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
    }

    fun choose_fav_number_from_list_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.searchbar_textfield)).perform(typeText(VALID_PHONE_NUMBER), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText(VALID_PHONE_NUMBER)))
        val viewInteraction = onView(withId(R.id.telco_search_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
    }

    fun validate_show_contents_pdp_telco_not_login() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_page_container)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_view_pager)).check(matches(isDisplayed()))
    }

    fun validate_interaction_promo() {
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText("")))

        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view_menu_component), withParent(withId(R.id.layout_widget)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(0,
                CommonActions.clickChildViewWithId(R.id.btn_copy_promo)))

        Thread.sleep(2000)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        viewInteraction.perform(RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(3,
                        CommonActions.clickChildViewWithId(R.id.promo_container)))
        Thread.sleep(3000)
    }

    fun validate_interaction_menu() {
        onView(withId(R.id.action_overflow_menu)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.menu_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_help)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_order_list)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }

    private fun pick_phone_number_from_phonebook() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_contact_picker_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_ac_input_number)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
    }

    /**
     * activate this test for local instrumentation test only because it contains contact picker
     */
    fun click_phonebook_and_clear() {
        stubContactNumber()

        Thread.sleep(2000)
        pick_phone_number_from_phonebook()
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText("")))
    }

    fun interaction_product_not_login() {
        Thread.sleep(2000)
        choose_fav_number_from_list_fav_number()

        Thread.sleep(3000)
        onView(withId(R.id.telco_view_pager)).check(matches(isDisplayed()))

        // click on product item on pulsa
        Thread.sleep(4000)
        onView(withId(R.id.telco_product_view)).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(isDisplayingAtLeast(30), withId(R.id.telco_product_rv))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))
        onView(withId(R.id.telco_buy_widget)).check(matches(isDisplayed()))

        //click tab roaming
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Roaming"))).perform(click())
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(8))
        Thread.sleep(3000)
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<TelcoProductViewHolder>(1))

        //click tab paket data, click lihat detail and close bottom sheet
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())
        onView(withId(R.id.telco_buy_widget)).check(matches(IsNot.not(isDisplayed())))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1,
                CommonActions.clickChildViewWithId(R.id.telco_see_more_btn)))
        Thread.sleep(1000)
        onView(withId(R.id.telco_button_select_item)).check(matches(isDisplayed()))
        onView(withId(R.id.bottom_sheet_close)).perform(click())

        //click product cluster on paket data
        onView(withId(R.id.telco_buy_widget)).check(matches(IsNot.not(isDisplayed())))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))
        onView(withId(R.id.telco_buy_widget)).check(matches(isDisplayed()))
    }

    fun interaction_product_filter() {
        //click tab paket data
        Thread.sleep(1000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Pulsa"))).perform(click())
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())

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

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY_NON_LOGIN = "tracker/recharge/recharge_telco_prepaid.json"
    }
}