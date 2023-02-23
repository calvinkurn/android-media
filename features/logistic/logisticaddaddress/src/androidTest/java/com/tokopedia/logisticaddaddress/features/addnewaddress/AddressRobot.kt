package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_REF
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert.assertThat

fun addAddress(func: AddressRobot.() -> Unit) = AddressRobot().apply(func)

class AddressRobot {

    fun launchFrom(rule: ActivityTestRule<PinpointMapActivity>, screenName: String) {
        val i = Intent()
        i.putExtra(EXTRA_REF, screenName)
        rule.launchActivity(i)
        waitForData()
    }

    fun searchWithKeyword(keyword: String) {
        onView(withId(R.id.layout_search)).perform(click())
        onView(withId(R.id.searchbar_textfield)).perform(typeText(keyword), closeSoftKeyboard())
        waitForData()
    }

    fun selectFirstItem() {
        onView(withId(R.id.rv_poi_list))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun addressDetail(detail: String) {
        onView(withId(R.id.et_detail_address))
                .perform(typeText(detail), closeSoftKeyboard())
        onView(withId(R.id.btn_choose_location)).perform(click())
    }

    fun clickCity() {
        onView(withId(R.id.et_kota_kecamatan_mismatch)).perform(click())
    }

    fun searchCityWithKeyword(keyword: String) {
        onView(withId(R.id.layout_search)).perform(click())
        onView(allOf(withId(R.id.searchbar_textfield), withEffectiveVisibility(Visibility.VISIBLE)))
                .check(matches(isDisplayed()))
                .perform(typeText(keyword), closeSoftKeyboard())
        waitForData()
    }

    fun selectFirstCityItem() {
        onView(withId(R.id.rv_list_district))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun zipCode() {
        onView(withId(R.id.et_kode_pos_mismatch)).perform(click())
    }

    fun selectFirstZipCode() {
        onView(withId(R.id.rv_kodepos_chips_mismatch))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    fun address(address: String) {
        onView(withId(R.id.et_alamat_mismatch))
                .perform(click(), typeText(address), closeSoftKeyboard())
    }

    fun receiver(receiver: String) {
        onView(withId(R.id.et_receiver_name))
                .perform(click(), typeText(receiver), closeSoftKeyboard())
    }

    fun phoneNumber(phone: String) {
        onView(withId(R.id.et_phone))
                .perform(scrollTo(), click(), typeText(phone), closeSoftKeyboard())
    }

    private fun waitForData() {
        Thread.sleep(1000L)
    }

    infix fun submit(func: ResultRobot.() -> Unit): ResultRobot {
        onView(withId(R.id.btn_save_address)).perform(scrollTo(), click())
        return ResultRobot().apply(func)
    }

}

class ResultRobot {
    fun hasPassedAnalytics(rule: CassavaTestRule, path: String) {
        assertThat(rule.validate(path), hasAllSuccess())
    }
}