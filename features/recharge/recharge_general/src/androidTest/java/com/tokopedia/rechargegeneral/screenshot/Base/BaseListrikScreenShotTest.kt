package com.tokopedia.rechargegeneral.screenshot.Base

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.common.topupbills.widget.TopupBillsInputDropdownWidget
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.RechargeGeneralLoginMockResponseConfig
import com.tokopedia.rechargegeneral.cases.RechargeGeneralProduct
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralInputViewHolder
import com.tokopedia.rechargegeneral.presentation.adapter.viewholder.RechargeGeneralProductSelectViewHolder
import com.tokopedia.rechargegeneral.widget.RechargeGeneralProductSelectBottomSheet
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonActions

abstract class BaseListrikScreenShotTest: BaseRechargeGeneralScreenShotTest() {

    override fun getMockConfig(): MockModelConfig = RechargeGeneralLoginMockResponseConfig(RechargeGeneralProduct.LISTRIK)

    override fun getMenuId(): Int = 113

    override fun getCategoryId(): Int = 3

    override fun run_specific_product_test() {
        select_operator()
        select_phone_number()
        select_product()
    }

    private fun select_operator() {
        // Click "Jenis Produk Listrik"
        Espresso.onView(ViewMatchers.withId(R.id.operator_select)).perform(ViewActions.click())

        Thread.sleep(2000)
        CommonActions.findViewAndScreenShot(
                R.id.view_container,
                filePrefix(),
                "recycler_view_operator_select"
        )

        // Choose "Token Listrik"
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.vg_input_dropdown_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<TopupBillsInputDropdownWidget.TopupBillsInputDropdownViewHolder>(
                        0, ViewActions.click()
                )
        )
    }

    private fun select_phone_number() {
        Espresso.onView(ViewMatchers.withId(R.id.rv_digital_product)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralInputViewHolder>(
                        0, ViewActions.click()
                )
        )
        Thread.sleep(2000)
        Espresso.closeSoftKeyboard()
        Thread.sleep(2000)

        CommonActions.findViewAndScreenShot(
                R.id.container_digital_search_number,
                filePrefix(),
                "favorite_number"
        )

        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withText("08121111111"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                .perform(ViewActions.click())
    }

    private fun select_product() {
        // Click "Nominal"
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.rv_digital_product)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectViewHolder>(
                        1, ViewActions.click()
                )
        )
        Thread.sleep(2000)

        CommonActions.findViewAndScreenShot(
                R.id.rv_product_select_dropdown,
                filePrefix(),
                "product_select"
        )

        Espresso.onView(ViewMatchers.withId(R.id.rv_product_select_dropdown)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<RechargeGeneralProductSelectBottomSheet.DigitalProductSelectDropdownAdapter.DigitalProductSelectDropdownViewHolder>(
                        1, ViewActions.click()
                )
        )
    }
}