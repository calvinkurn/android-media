package com.tokopedia.topupbills

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.provider.ContactsContract
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultCode
import androidx.test.espresso.contrib.ActivityResultMatchers.hasResultData
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CLIENT_NUMBER
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_CLIENT_NUMBER_TYPE
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity.Companion.EXTRA_NUMBER_LIST
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.test.application.matcher.RecyclerViewMatcher
import com.tokopedia.topupbills.searchnumber.view.DigitalSearchNumberActivity
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class DigitalSearchNumberActivityTest {

    @get:Rule
    var mRuntimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.READ_CONTACTS)

    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalSearchNumberActivity> =
            object : IntentsTestRule<DigitalSearchNumberActivity>(DigitalSearchNumberActivity::class.java) {
                override fun getActivityIntent(): Intent {
                    val favNumbers = generateFavoriteNumber()
                    val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                    return Intent(targetContext, DigitalSearchNumberActivity::class.java).apply {
                        putExtra(EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL)
                        putExtra(EXTRA_CLIENT_NUMBER, "")
                        putParcelableArrayListExtra(EXTRA_NUMBER_LIST, favNumbers)
                    }
                }
            }

    fun stubSearchNumberActivityResult(): Intent {
        val resultData = Intent()
        resultData.putExtra(
            TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER,
            generateFavoriteNumber()[0]
        )
        resultData.putExtra(
            TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE,
            TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE)
        return resultData
    }

    @Test
    fun validate_digital_search_number_page() {
        click_phonebook_and_navigate_pdp()
        show_contents_search_number_page()
        type_new_client_number()
        search_existing_favorite_number()
        click_favorite_number()
    }

    fun show_contents_search_number_page() {
        onView(withId(R.id.telco_search_number_input_view)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_search_number_rv)).check(matches(isDisplayed()))
        onView(withId(R.id.telco_search_number_contact_picker)).check(matches(isDisplayed()))
    }

    fun type_new_client_number() {
        typeNumberOnSearchBar(VALID_PHONE_NUMBER_NEW)
        validateFirstNumberInList(VALID_PHONE_NUMBER_NEW)
        validateRecyclerViewSize(1)
    }

    fun search_existing_favorite_number() {
        clickClearSearchBar()
        typeNumberOnSearchBar(VALID_PHONE_NUMBER_PREFIX)
        validateFirstNumberInList(VALID_PHONE_NUMBER_PREFIX)
        validateRecyclerViewSize(2)

        clickClearSearchBar()
        typeNumberOnSearchBar(VALID_PHONE_NUMBER_EXISTING)
        validateFirstNumberInList(VALID_PHONE_NUMBER_EXISTING)
        validateRecyclerViewSize(1)
    }

    fun click_phonebook_and_navigate_pdp() {
        Thread.sleep(2000)
        Intents.intending(AllOf.allOf(hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
            hasAction(Intent.ACTION_PICK))
        ).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        onView(withId(R.id.telco_search_number_contact_picker)).perform(click())
        intended(AllOf.allOf(hasData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
            hasAction(Intent.ACTION_PICK)))
    }

    fun click_favorite_number() {
        Intents.intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, stubSearchNumberActivityResult()))
        val viewInteraction = onView(AllOf.allOf(ViewMatchers.isDisplayingAtLeast(30), withId(R.id.telco_search_number_rv))).check(matches(isDisplayed()))
        viewInteraction.perform(actionOnItemAtPosition<TelcoProductViewHolder>(0, click()))
        validateIntentExtraParam()
    }

    private fun clickClearSearchBar() {
        onView(withId(R.id.searchbar_icon)).perform(click())
        onView(withId(R.id.searchbar_textfield)).check(matches(withText("")))
    }

    private fun typeNumberOnSearchBar(number: String) {
        onView(withId(R.id.searchbar_textfield))
            .perform(typeText(number), closeSoftKeyboard())
    }

    private fun validateFirstNumberInList(number: String) {
        onView(RecyclerViewMatcher(R.id.telco_search_number_rv).atPositionOnView(0, R.id.text_name))
            .check(matches(withText(number)))
    }

    private fun validateRecyclerViewSize(size: Int){
        assert(getRecyclerViewItemCount(R.id.telco_search_number_rv) == size)
    }

    private fun validateIntentExtraParam() {
        assertThat(mActivityRule.activityResult, hasResultCode(Activity.RESULT_OK));
        assertThat(mActivityRule.activityResult, hasResultData(IntentMatchers.hasExtraWithKey(
            TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER
        )))
        assertThat(mActivityRule.activityResult, hasResultData(IntentMatchers.hasExtraWithKey(
            TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE
        )))
    }

    private fun getRecyclerViewItemCount(resId: Int): Int {
        val recyclerView = mActivityRule.activity.findViewById<RecyclerView>(resId)
        return recyclerView?.adapter?.itemCount ?: 0
    }

    companion object {
        private const val VALID_PHONE_NUMBER_NEW = "08123232323"
        private const val VALID_PHONE_NUMBER_EXISTING = "081208120812"
        private const val VALID_PHONE_NUMBER_PREFIX = "0812"

        fun generateFavoriteNumber() = arrayListOf(
            TopupBillsFavNumberItem(clientNumber = "081208120812"),
            TopupBillsFavNumberItem(clientNumber = "085708570857"),
            TopupBillsFavNumberItem(clientNumber = "081908190819"),
            TopupBillsFavNumberItem(clientNumber = "081933333333"),
        )
    }
}