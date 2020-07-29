package com.tokopedia.topupbills

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
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import org.hamcrest.core.AllOf
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
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
    }

    @Before
    fun stubAllExternalIntents() {
        gtmLogDBSource.deleteAll().subscribe()

        val telcoContactHelper = TelcoContactHelper()
        val contentResolver = mActivityRule.activity.contentResolver

        Intents.intending(AllOf.allOf(IntentMatchers.hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                IntentMatchers.hasAction(Intent.ACTION_PICK))
        ).respondWith(telcoContactHelper.createUriContact(contentResolver))

        Intents.intending(AllOf.allOf(IntentMatchers.hasComponent(
                ComponentNameMatchers.hasShortClassName(".DigitalSearchNumberFragment"))))
                .respondWith(createOrderNumberTypeManual())
    }

    private fun createOrderNumberTypeManual(): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsFavNumberItem(clientNumber = VALID_PHONE_NUMBER)
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
                TopupBillsSearchNumberFragment.InputNumberActionType.MANUAL)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    @Test
    fun validate_prepaid() {
        show_contents_pdp_telco_not_login()
        copy_promo_code()
        interaction_menu()
        click_on_contact_picker_and_list_fav_number()
        click_done_keyboard_fav_number()
        choose_fav_number_from_list_fav_number()
        click_phonebook_and_clear()
        interaction_product_not_login()
        interaction_product_filter()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY),
                hasAllSuccess())
    }

    /**
     * activate the comment below if the test is new on the device
     */
    fun show_contents_pdp_telco_not_login() {
//        Thread.sleep(1000)
//        onView(withText(R.string.Telco_title_showcase_client_number)).check(matches(isDisplayed()))
//        onView(withText(R.string.Telco_title_showcase_client_number)).perform(click())
//        onView(withText(R.string.telco_title_showcase_promo)).check(matches(isDisplayed()))
//        onView(withText(R.string.telco_title_showcase_promo)).perform(click())
        Thread.sleep(2000)
        onView(withId(R.id.page_container)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))
    }

    fun copy_promo_code() {
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.layout_widget)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0,
                CommonActions.clickChildViewWithId(R.id.btn_copy_promo)))
    }

    fun interaction_menu() {
        onView(withId(R.id.action_overflow_menu)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.menu_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_help)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_order_list)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.btn_close)).perform(click())
    }

    fun click_done_keyboard_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.image_button_close)).perform(click())
        onView(withId(R.id.edit_text_search)).check(matches(withText("")))
        onView(withId(R.id.edit_text_search)).perform(typeText(VALID_PHONE_NUMBER), pressImeActionButton())
        onView(withId(R.id.ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
    }

    fun click_on_contact_picker_and_list_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.edit_text_search)).check(matches(withText("")))
        onView(withId(R.id.btnContactPicker)).perform(click())
        onView(withId(R.id.edit_text_search)).check(matches(isDisplayed()))
        onView(withId(R.id.edit_text_search)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
        val viewInteraction = onView(withId(R.id.rvNumberList)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.ac_input_number)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
    }

    fun choose_fav_number_from_list_fav_number() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_input_number)).perform(click())
        onView(withId(R.id.image_button_close)).perform(click())
        onView(withId(R.id.edit_text_search)).check(matches(withText("")))
        onView(withId(R.id.edit_text_search)).perform(typeText(VALID_PHONE_NUMBER), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.edit_text_search)).check(matches(withText(VALID_PHONE_NUMBER)))
        val viewInteraction = onView(withId(R.id.rvNumberList)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.ac_input_number)).check(matches(withText(VALID_PHONE_NUMBER)))
    }

    private fun pick_phone_number_from_phonebook() {
        Thread.sleep(2000)
        onView(withId(R.id.btn_contact_picker)).perform(click())
        onView(withId(R.id.ac_input_number)).check(matches(isDisplayed()))
        onView(withId(R.id.ac_input_number)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
    }

    fun click_phonebook_and_clear() {
        Thread.sleep(2000)
        pick_phone_number_from_phonebook()
        onView(withId(R.id.btn_clear_input_number)).perform(click())
        onView(withId(R.id.ac_input_number)).check(matches(withText("")))
    }

    fun interaction_product_not_login() {
        Thread.sleep(2000)
        pick_phone_number_from_phonebook()

        Thread.sleep(3000)
        onView(withId(R.id.view_pager)).check(matches(isDisplayed()))

        // click on product item on pulsa
        Thread.sleep(4000)
        onView(withId(R.id.telco_product_view)).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(isDisplayingAtLeast(30), withId(R.id.product_recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))
        onView(withId(R.id.buy_widget)).check(matches(isDisplayed()))

        //click tab roaming
        Thread.sleep(1000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Roaming"))).perform(click())

        //click tab paket data, click lihat detail and close bottom sheet
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())
        onView(withId(R.id.buy_widget)).check(matches(IsNot.not(isDisplayed())))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1,
                CommonActions.clickChildViewWithId(R.id.telco_see_more_btn)))
        Thread.sleep(1000)
        onView(withId(R.id.button_select_item)).check(matches(isDisplayed()))
        onView(withId(R.id.bottom_sheet_close)).perform(click())

        //click product cluster on paket data
        onView(withId(R.id.buy_widget)).check(matches(IsNot.not(isDisplayed())))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(1, click()))
        onView(withId(R.id.buy_widget)).check(matches(isDisplayed()))
    }

    fun interaction_product_filter() {
        //click tab paket data
        Thread.sleep(1000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Pulsa"))).perform(click())
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Paket Data"))).perform(click())
        onView(AllOf.allOf(isDisplayed(), withId(R.id.sort_filter))).check(matches(isDisplayed()))

        //click Feature filter and choose 1 subfilter
        Thread.sleep(3000)
        onView(AllOf.allOf(withText("Feature"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper)))).perform(click())
        Thread.sleep(3000)
        onView(withId(R.id.filter_recycler_view)).check(matches(isDisplayed()))
        onView(withText(R.string.telco_reset_filter)).check(matches(isDisplayed()))
        val viewInteraction = onView(AllOf.allOf(isDisplayed(), withId(R.id.filter_recycler_view))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.btn_filter)).perform(click())
        onView(AllOf.allOf(isDisplayed(), withId(R.id.sort_filter))).check(matches(isDisplayed()))
        Thread.sleep(5000)

        //click on Feature filter and reset subfilter
        Thread.sleep(3000)
        onView(AllOf.allOf(withText("Feature"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper)))).perform(click())
        Thread.sleep(3000)
        onView(withText(R.string.telco_reset_filter)).check(matches(isDisplayed()))
        onView(withText(R.string.telco_reset_filter)).perform(click())
        onView(withId(R.id.btn_filter)).perform(click())

        //click clear cluster filter selected
        Thread.sleep(3000)
        onView(AllOf.allOf(withText("Kuota"), isDescendantOfA(withId(R.id.sort_filter_items_wrapper)))).perform(click())
        Thread.sleep(3000)
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.btn_filter)).perform(click())
        onView(withId(R.id.sort_filter_prefix)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(AllOf.allOf(isDisplayed(), withId(R.id.sort_filter))).check(matches(isDisplayed()))
        onView(withId(R.id.sort_filter_prefix)).perform(click())
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY = "tracker/recharge/recharge_telco_prepaid.json"
    }
}