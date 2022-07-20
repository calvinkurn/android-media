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
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_REF
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
        waitForData()
    }

    fun clickAddressStreetItem() {
        onView(withId(R.id.rv_address_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        waitForData()
    }

    fun onClickChooseLocation() {
        onView(withId(R.id.btn_primary)).perform(click())
        waitForData()
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

    fun clickManualForm() {
        onView(withId(R.id.tv_message_search)).perform(click())
    }

    fun clickKotaKecamatan() {
        onView(withId(R.id.et_kota_kecamatan)).perform(click())
        waitForData()
    }

    fun searchKotaKecamatan(keyword: String) {
        onView(withId(R.id.search_page_input)).perform(click())
        onView(withId(R.id.searchbar_textfield))
                .perform(click(), typeText(keyword), closeSoftKeyboard())
        waitForData()
    }

    fun clickKotaKecamatanItem() {
        onView(withId(R.id.rv_list_district))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun clickPostalCode() {
        onView(withId(R.id.et_kodepos))
                .perform(click(), closeSoftKeyboard())
    }

    fun clickPostalCodeItem() {
        onView(withId(R.id.rv_kodepos_chips))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()), closeSoftKeyboard())
    }

    fun clickChoosePostalCode() {
        onView(withId(R.id.btn_choose_zipcode))
                .perform(click())
    }

    fun fillAddressNegative(address: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_alamat))))
                .perform(click(), replaceText(address), closeSoftKeyboard())
    }

    private fun waitForData() {
        Thread.sleep(1000L)
    }

    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        onView(withId(R.id.btn_save_address_new)).perform(scrollTo(), click())
        return ResultRobot().apply(func)
    }

}

class ResultRobot {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }
}

fun addAddressRevamp(func: AddressRevampRobot.() -> Unit) = AddressRevampRobot().apply(func)