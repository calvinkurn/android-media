package com.tokopedia.topupbills.postpaid

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
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
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
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import org.hamcrest.core.AllOf
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsNot
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPostpaidInstrumentTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule: IntentsTestRule<TelcoPostpaidActivity> = object : IntentsTestRule<TelcoPostpaidActivity>(TelcoPostpaidActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return Intent(targetContext, TelcoPostpaidActivity::class.java).apply {
                putExtra(BaseTelcoActivity.PARAM_MENU_ID, TelcoComponentType.TELCO_POSTPAID.toString())
                putExtra(BaseTelcoActivity.PARAM_CATEGORY_ID, TelcoCategoryType.CATEGORY_PASCABAYAR.toString())
                putExtra(BaseTelcoActivity.PARAM_PRODUCT_ID, "")
                putExtra(BaseTelcoActivity.PARAM_CLIENT_NUMBER, "")
            }
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().toBlocking().first()

            setupGraphqlMockResponseWithCheck(TelcoPostpaidMockResponseConfig())
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
    fun validate_postpaid_non_login() {
        stubSearchNumber()

        validate_show_contents_pdp_telco_not_login()
        validate_interaction_menu()
        validate_click_done_keyboard_fav_number()
//        validate_click_on_contact_picker_and_list_fav_number()
//        click_phonebook_and_clear()
        choose_fav_number_from_list_fav_number()
        enquiry_phone_number()
        validate_interaction_promo()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_NON_LOGIN),
                hasAllSuccess())
    }

    fun validate_click_done_keyboard_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        onView(withId(R.id.searchbar_textfield)).perform(typeText(VALID_PHONE_NUMBER), pressImeActionButton())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
    }

    /**
     * activate this test for local instrumentation test only because it contains contact picker
     */
    fun validate_click_on_contact_picker_and_list_fav_number() {
        stubContactNumber()

        Thread.sleep(2000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
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
        onView(withId(R.id.telco_ac_input_number)).perform(click())
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

    fun enquiry_phone_number() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Thread.sleep(2000)
        onView(withId(R.id.telco_enquiry_btn)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.telco_enquiry_btn)).check(matches(IsNot.not(isDisplayed())))
        onView(withId(R.id.telco_title_enquiry_result)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_buy_widget)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_buy_widget)).perform(click())
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY_NON_LOGIN = "tracker/recharge/recharge_telco_postpaid.json"
    }
}