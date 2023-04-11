package com.tokopedia.topupbills.utils

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.common.topupbills.favoritepage.view.activity.TopupBillsPersoSavedNumberActivity
import com.tokopedia.common.topupbills.favoritepage.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.topupbills.R
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf

object CommonTelcoActions {

    private fun createOrderNumberWithType(
        number: String,
        type: TopupBillsSearchNumberFragment.InputNumberActionType,
        categoryId: String
    ): Instrumentation.ActivityResult {
        val savedNumber = if (type == TopupBillsSearchNumberFragment.InputNumberActionType.FAVORITE) {
            TopupBillsSavedNumber(
                clientName = "tokopedia",
                clientNumber = number,
                inputNumberActionTypeIndex = type.ordinal,
                categoryId = categoryId,
                productId = "81"
            )
        } else {
            TopupBillsSavedNumber(
                clientName = "tokopedia",
                clientNumber = number,
                inputNumberActionTypeIndex = type.ordinal
            )
        }
        val resultData = Intent()
        resultData.putExtra(
            TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER,
            savedNumber
        )
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    fun stubAccessingSavedNumber(
        number: String,
        type: TopupBillsSearchNumberFragment.InputNumberActionType,
        categoryId: String
    ) {
        Intents.intending(
            IntentMatchers.hasComponent(
                ComponentNameMatchers.hasClassName(TopupBillsPersoSavedNumberActivity::class.java.name)
            )
        )
            .respondWith(createOrderNumberWithType(number, type, categoryId))
    }

    fun clientNumberWidget_typeNumber(number: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .perform(typeText(number))
    }

    fun clientNumberWidget_validateText(text: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .check(matches(ViewMatchers.withText(text)))
    }

    fun clientNumberWidget_scrollToChip_withText(text: String) {
        onView(ViewMatchers.withText(text)).perform(scrollTo())
    }

    fun clientNumberWidget_clickFilterChip_withText(text: String) {
        onView(ViewMatchers.withText(text)).perform(click())
    }

    fun kebabMenu_validateContents() {
        onView(withId(R.id.menu_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_help)).check(matches(isDisplayed()))
        onView(withId(R.id.menu_order_list)).check(matches(isDisplayed()))
    }

    fun clientNumberWidget_clickClearBtn() {
        onView(withId(R.id.text_field_icon_close)).perform(click())
    }

    fun clientNumberWidget_clickContactBook() {
        onView(withId(R.id.text_field_icon_1)).perform(click())
    }

    fun kebabMenu_click() {
        onView(withId(R.id.action_overflow_menu)).perform(click())
    }

    fun promoItem_clickCopyButton(viewInteraction: ViewInteraction) {
        viewInteraction.perform(
            RecyclerViewActions.actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                0,
                CommonActions.clickChildViewWithId(R.id.btn_copy_promo)
            )
        )
    }

    fun promoItem_click(viewInteraction: ViewInteraction) {
        viewInteraction.perform(
            RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                    3,
                    CommonActions.clickChildViewWithId(R.id.promo_container)
                )
        )
    }

    fun pdp_validateBuyWidgetDisplayed() {
        onView(withId(R.id.telco_buy_widget)).check(matches(isDisplayed()))
    }

    fun pdp_validateBuyWidgetNotDisplayed() {
        onView(withId(R.id.telco_buy_widget)).check(matches(not(isDisplayed())))
    }

    fun pdp_clickBuyWidget() {
        onView(withId(R.id.telco_buy_widget)).perform(click())
    }

    fun tabLayout_clickTabWithText(text: String) {
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), ViewMatchers.withText(text))).perform(
            click()
        )
    }

    fun bottomSheet_close() {
        onView(withId(R.id.bottom_sheet_close)).perform(click())
    }
}
