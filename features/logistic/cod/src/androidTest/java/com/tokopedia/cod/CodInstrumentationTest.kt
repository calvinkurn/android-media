package com.tokopedia.cod

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.google.gson.Gson
import com.tokopedia.cod.view.CodActivity
import com.tokopedia.transactiondata.entity.response.cod.Data
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 * Able to execute if Dagger was not injected
 * [FAILED] Unable to execute espresso test if:
 * - run at module level -> cannot cast running espresso application to BaseMainApplication
 * - run at app level -> got many errors from other test cases at androidTest source set
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class CodInstrumentationTest {

    val COUNTER_INFO = "You have quota of 6 times to pay with Bayar di Tempat. Free Bayar di Tempat fee for the first 3 transactions."
    val MESSAGE_INFO = "Complete the payment via courier after receiving your order."

    @get:Rule val mActivityRule
            = ActivityTestRule(CodActivity::class.java, true, false)

    @Before
    fun intentWithFakeData() {
        val startIntent = Intent()
        startIntent.putExtra(CodActivity.EXTRA_COD_DATA, Gson().fromJson(provideData(), Data::class.java))
        mActivityRule.launchActivity(startIntent)
    }

    @Test
    fun whenSuccessContractDisplayCorrectData() {
        onView(withId(R.id.textview_counter_info))
                .check(matches(withText(COUNTER_INFO)))
        onView(withId(R.id.textview_ticker_message))
                .check(matches(withText(MESSAGE_INFO)))
    }

    fun provideData(): String {
        return FileUtils().getJsonFromAsset("cod_data_contract")
    }
}
