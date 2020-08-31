package com.tokopedia.topupbills.postpaid

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.ContactsContract
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponseWithCheck
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.TelcoContactHelper
import com.tokopedia.topupbills.telco.common.activity.BaseTelcoActivity
import com.tokopedia.topupbills.telco.data.constant.TelcoCategoryType
import com.tokopedia.topupbills.telco.data.constant.TelcoComponentType
import com.tokopedia.topupbills.telco.postpaid.activity.TelcoPostpaidActivity
import com.tokopedia.topupbills.telco.prepaid.activity.TelcoPrepaidActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import com.tokopedia.topupbills.telco.prepaid.fragment.DigitalTelcoPrepaidFragment
import org.hamcrest.core.AllOf
import org.hamcrest.core.AnyOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TelcoPostpaidLoginInstrumentTest {

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
            setupGraphqlMockResponseWithCheck(TelcoPostpaidLoginMockResponseConfig())
        }
    }

    @Before
    fun stubAllExternalIntents() {
        gtmLogDBSource.deleteAll().subscribe()

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
    fun validate_postpaid_login() {
        stubSearchNumber()
        stubContactNumber()
        InstrumentationAuthHelper.loginInstrumentationTestUser1(mActivityRule.activity.application)

        Thread.sleep(3000)

        click_on_fav_number_login()
        enquiry_phone_number()
        click_on_tab_menu_login()
        click_item_recent_widget_login()

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_LOGIN),
                hasAllSuccess())
    }

    fun click_on_fav_number_login() {
        Thread.sleep(2000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
        onView(withId(R.id.telco_search_number_contact_picker)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(isDisplayed()))
        onView(withId(R.id.searchbar_textfield)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))
        val viewInteraction = onView(withId(R.id.telco_search_number_rv)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        onView(withId(R.id.telco_ac_input_number)).check(matches(AnyOf.anyOf(withText(VALID_PHONE_BOOK), withText(VALID_PHONE_BOOK_RAW))))

        Thread.sleep(2000)
        onView(withId(R.id.telco_ac_input_number)).perform(click())
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
    }

    fun click_on_tab_menu_login() {
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
        onView(withId(R.id.telco_ac_input_number)).check(matches(withText("")))
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Recents"))).perform(click())
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Promo"))).perform(click())
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

    fun click_item_recent_widget_login() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Recents"))).perform(click())
        val viewInteraction = onView(AllOf.allOf(isDescendantOfA(withId(R.id.layout_widget)),
                withId(R.id.recycler_view_menu_component), isDisplayed())).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(0, click()))
        Thread.sleep(3000)
        enquiry_phone_number()
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val VALID_PHONE_NUMBER = "08123232323"
        private const val VALID_PHONE_BOOK = "087821212121"
        private const val VALID_PHONE_BOOK_RAW = "0878-2121-2121"
        private const val ANALYTIC_VALIDATOR_QUERY_LOGIN = "tracker/recharge/recharge_telco_postpaid_login.json"
    }
}