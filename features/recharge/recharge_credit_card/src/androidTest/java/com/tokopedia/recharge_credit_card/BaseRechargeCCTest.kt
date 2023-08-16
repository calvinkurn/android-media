package com.tokopedia.recharge_credit_card

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.common.topupbills.view.adapter.TopupBillsPromoListAdapter
import com.tokopedia.common.topupbills.view.adapter.TopupBillsRecentNumbersAdapter
import com.tokopedia.test.application.espresso_component.CommonActions
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.AllOf

abstract class BaseRechargeCCTest {

    protected fun clientNumberWidget_typeCreditCardNumber(ccNumber: String) {
        onView(
            allOf(
                supportsInputMethods(),
                isDescendantOfA(withId(R.id.cc_widget_client_number))
            )
        ).perform(typeText(ccNumber))
    }

    protected fun clientNumberWidget_clickLanjutButton() {
        onView(withId(R.id.client_number_widget_button)).perform(click())
    }

    protected fun confirmationDialog_clickCloseButton() {
        onView(
            allOf(
                withText(R.string.cc_cta_btn_secondary),
                isDisplayed()
            )
        ).perform(click())
    }

    protected fun confirmationDialog_clickConfirmButton() {
        onView(allOf(withText(R.string.cc_cta_btn_primary), isDisplayed()))
            .perform(click())
    }

    protected fun pdp_clickBankListButton() {
        onView(withId(R.id.cc_widget_bank_list)).perform(click())
    }

    protected fun bankListBottomSheet_clickCloseButton() {
        onView(withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)).perform(click())
    }

    protected fun clientNumberWidget_checkIsButtonDisabled() {
        onView(withId(R.id.client_number_widget_button)).check(matches(not(isEnabled())))
    }

    protected fun clientNumberWidget_checkIsButtonEnabled() {
        onView(withId(R.id.client_number_widget_button)).check(matches(isEnabled()))
    }

    protected fun clientNumberWidget_clickClearIcon() {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_icon_close)).perform(click())
    }

    protected fun clientNumberWidget_checkIsShowInvalidNumberError() {
        onView(withText(R.string.cc_error_invalid_number)).check(matches(isDisplayed()))
    }

    protected fun clientNumberWidget_checkIsShowBankIsNotSupportedError() {
        onView(withText(R.string.cc_bank_is_not_supported)).check(matches(isDisplayed()))
    }

    protected fun clientNumberWidget_checkIsOperatorIconShown() {
        onView(withText(R.id.client_number_widget_operator_icon)).isVisible()
    }

    protected fun clientNumberWidget_checkIsOperatorIconHidden() {
        onView(withText(R.id.client_number_widget_operator_icon)).isInvisible()
    }

    protected fun clientNumberWidget_checkIsLoadingStateShown() {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_loader)).check(matches(isDisplayed()))
    }

    protected fun clientNumberWidget_checkIsLoadingStateHidden() {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_loader)).check(matches(not(isDisplayed())))
    }

    protected fun tabLayout_clickTabWithText(text: String) {
        onView(AllOf.allOf(withId(com.tokopedia.unifycomponents.R.id.tab_item_text_id), ViewMatchers.withText(text))).perform(
            click()
        )
    }

    protected fun recentTransaction_scrollToPosition(position: Int) {
        val viewInteraction = onView(
            AllOf.allOf(
                isDescendantOfA(withId(com.tokopedia.common.topupbills.R.id.layout_widget)),
                withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component),
                isDisplayed()
            )
        )
        viewInteraction.perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    protected fun promo_scrollToPosition(position: Int) {
        val viewInteraction = onView(
            AllOf.allOf(
                isDescendantOfA(withId(com.tokopedia.common.topupbills.R.id.layout_widget)),
                withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component),
                isDisplayed()
            )
        )
        viewInteraction.perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }

    protected fun recentTransaction_clickItemWithPosition(position: Int) {
        val viewInteraction = onView(
            AllOf.allOf(
                isDescendantOfA(withId(com.tokopedia.common.topupbills.R.id.layout_widget)),
                withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component),
                isDisplayed()
            )
        )
        viewInteraction.perform(
            RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsRecentNumbersAdapter.RecentNumbersItemViewHolder>(
                    position,
                    click()
                )
        )
    }

    protected fun promo_clickCopyPromoWithPosition(position: Int) {
        val viewInteraction = onView(
            AllOf.allOf(
                isDescendantOfA(withId(com.tokopedia.common.topupbills.R.id.layout_widget)),
                withId(com.tokopedia.common.topupbills.R.id.recycler_view_menu_component),
                isDisplayed()
            )
        )
        viewInteraction.perform(
            RecyclerViewActions
                .actionOnItemAtPosition<TopupBillsPromoListAdapter.PromoItemViewHolder>(
                    position,
                    CommonActions.clickChildViewWithId(com.tokopedia.common.topupbills.R.id.btn_copy_promo)
                )
        )
    }

    private fun ViewInteraction.isVisible() = getViewAssertion(ViewMatchers.Visibility.VISIBLE)

    private fun ViewInteraction.isInvisible() = getViewAssertion(ViewMatchers.Visibility.INVISIBLE)

    private fun getViewAssertion(visibility: ViewMatchers.Visibility): ViewAssertion? {
        return matches(ViewMatchers.withEffectiveVisibility(visibility))
    }

    companion object {
        internal const val VALID_CC_NUMBER = "4111111111111111"
        internal const val VALID_AMEX_CC_NUMBER = "371449635398431"
        internal const val INVALID_CC_NUMBER = "4111111111111888"
        internal const val INVALID_CC_NUMBER_2 = "8888888888888888"

        internal const val KEY_QUERY_BANK_LIST = "rechargeBankList"
        internal const val KEY_QUERY_MENU_DETAIL = "catalogMenuDetail"
        internal const val KEY_QUERY_PREFIXES = "catalogPrefix"

        internal const val PATH_RESPONSE_RECHARGE_BANK_LIST = "response_mock_data_cc_bank_list.json"
        internal const val PATH_RESPONSE_RECHARGE_CATALOG_MENU_DETAIL = "response_mock_data_cc_menu_detail.json"
        internal const val PATH_RESPONSE_RECHARGE_CATALOG_PREFIXES = "response_mock_data_cc_prefixes.json"
    }
}
