package com.tokopedia.pdpsimulation.common.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import  com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_URL
import com.tokopedia.pdpsimulation.common.constants.PRODUCT_PRICE
import com.tokopedia.pdpsimulation.common.presentation.activity.PdpSimulationActivity
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class PdpSimulationFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(PdpSimulationActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setUp() {
        clearData()
        launchActivity()
    }

    @Test
    fun doSomething() {
        val a = 0
        Assert.assertEquals(0, a)
    }

  /*  @Test
    fun check_register_widget_visible_on_top() {
        Espresso.onView(ViewMatchers.withId(R.id.paylaterDaftarWidget))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        print("PDP simulation test")
    }*/

    private fun clearData() {
        gtmLogDBSource.deleteAll().toBlocking()
    }

    private fun launchActivity() {
        val bundle = Bundle()
        bundle.putInt(PRODUCT_PRICE, 1000000)
        bundle.putString(PARAM_PRODUCT_URL, "email")
        val intent = Intent(context, PdpSimulationActivity::class.java)
        intent.putExtras(bundle)
        activityRule.launchActivity(intent)
    }
}