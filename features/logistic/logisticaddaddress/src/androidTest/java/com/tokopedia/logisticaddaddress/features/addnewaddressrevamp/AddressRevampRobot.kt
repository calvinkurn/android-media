package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.EXTRA_REF
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.allOf

class AddressRevampRobot {

    fun launchWithParam(rule: ActivityTestRule<SearchPageActivity>, screenName: String) {
        val i = Intent()
        i.putExtra(EXTRA_REF, screenName)
        rule.launchActivity(i)
    }

    fun searchAddressStreet(keyword: String) {
        onView(withId(R.id.search_page_input)).perform(click())
        onView(withId(R.id.searchbar_textfield)).perform(typeText(keyword), pressImeActionButton())
        Thread.sleep(1000L)
    }

    fun clickAddressStreetItem() {
        val i = Intent().apply { putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true) }
        onView(withId(R.id.rv_address_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun onClickChooseLocation() {
        onView(withId(R.id.btn_primary)).perform(click())
        Thread.sleep(1000L)
    }

    fun fillAddress(address: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_alamat_new))))
                .perform(click(), typeText(address), closeSoftKeyboard())
    }

    fun fillReceiver(receiver: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_nama_penerima))))
                .perform(click(), typeText(receiver), closeSoftKeyboard())
    }

    fun fillPhoneNumber(phone: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_nomor_hp))))
                .perform(click(), typeText(phone), closeSoftKeyboard())
    }

    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        onView(withId(R.id.btn_save_address_new)).perform(click())
        return ResultRobot().apply(func)
    }

}

class ResultRobot {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }
}

fun addAddressRevamp(func: AddressRevampRobot.() -> Unit) = AddressRevampRobot().apply(func)