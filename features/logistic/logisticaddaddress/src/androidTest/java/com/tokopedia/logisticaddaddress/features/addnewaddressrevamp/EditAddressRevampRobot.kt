package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert

class EditAddressRevampRobot {

    fun launchWithParam(context: Context, rule: ActivityTestRule<AddressFormActivity>) {
        val i = RouteManager.getIntent(context, "${ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP}/12345")
        rule.launchActivity(i)
        waitForData()
    }

    fun fillAddress(address: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_alamat_new))))
            .perform(click(), clearText(), typeText(address), closeSoftKeyboard())
    }

    fun fillPhoneNumber(phone: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_nomor_hp))))
            .perform(click(), clearText(), typeText(phone), closeSoftKeyboard())
    }

    fun fillReceiver(receiver: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_nama_penerima))))
            .perform(click(), clearText(), typeText(receiver), closeSoftKeyboard())
    }

    fun onClickChangePinpoint() {
        onView(withId(R.id.btn_change)).perform(click())
        waitForData(3000)
    }

    fun onClickCariUlangAlamat() {
        onView(withId(R.id.chips_search)).perform(click())
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

    infix fun submit(func: EditAddressResultRobot.() -> Unit): EditAddressResultRobot {
        onView(withId(R.id.btn_save_address_new)).perform(scrollTo(), click())
        return EditAddressResultRobot().apply(func)
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
            .perform(click(), replaceText(keyword), closeSoftKeyboard())
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
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()),
                closeSoftKeyboard()
            )
    }

    fun clickChoosePostalCode() {
        onView(withId(R.id.btn_choose_zipcode))
            .perform(click())
        waitForData()
    }

    fun fillAddressNegative(address: String) {
        onView(allOf(withId(R.id.text_field_input), isDescendantOfA(withId(R.id.et_alamat))))
            .perform(click(), typeText(address), closeSoftKeyboard())
    }

    private fun waitForData(millis: Long = 1000L) {
        Thread.sleep(millis)
    }

}

class EditAddressResultRobot {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        MatcherAssert.assertThat(rule.validate(path), hasAllSuccess())
    }
}

fun editAddressRevamp(func: EditAddressRevampRobot.() -> Unit) =
    EditAddressRevampRobot().apply(func)
