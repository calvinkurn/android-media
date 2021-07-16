package com.tokopedia.topupbills.utils

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.data.TopupBillsSeamlessFavNumberItem
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.prepaid.adapter.viewholder.TelcoProductViewHolder
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot

object CommonTelcoActions {

    fun createOrderNumberWithType(number: String, type: TopupBillsSearchNumberFragment.InputNumberActionType): Instrumentation.ActivityResult {
        val orderClientNumber = TopupBillsSeamlessFavNumberItem(clientNumber = number)
        val resultData = Intent()
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, orderClientNumber)
        resultData.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE, type.ordinal)
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    fun stubSearchNumber(number: String, type: TopupBillsSearchNumberFragment.InputNumberActionType) {
        Intents.intending(IntentMatchers.isInternal()).respondWith(createOrderNumberWithType(number, type))
    }

    fun clickClientNumberWidget() {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun validateTextClientNumberWidget(text: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .check(matches(ViewMatchers.withText(text)))
    }

    fun validate3MenuContents() {
        onView(withId(R.id.menu_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_help)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_order_list)).check(matches(isDisplayed()))
    }

    fun validateViewPagerDisplayed() {
        onView(withId(R.id.telco_view_pager)).check(matches(isDisplayed()))
    }

    fun clickClearBtn() {
        onView(withId(R.id.telco_clear_input_number_btn)).perform(click())
    }

    fun click3DotsMenu() {
        onView(withId(R.id.action_overflow_menu)).perform(click())
    }

    fun clickCopyPromoBtn(viewInteraction: ViewInteraction) {
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(0,
            CommonActions.clickChildViewWithId(R.id.btn_copy_promo)))
    }

    fun clickPromoItem(viewInteraction: ViewInteraction) {
        viewInteraction.perform(RecyclerViewActions
            .actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(3,
                CommonActions.clickChildViewWithId(R.id.promo_container)))
    }
}