package com.tokopedia.digital_product_detail

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.applink.RouteManager
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.digital_product_detail.presentation.activity.DigitalPDPPulsaActivity
import com.tokopedia.digital_product_detail.pulsa.utils.DigitalPDPPulsaMockConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule

abstract class BaseDigitalPDPPulsaTest {

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    @get:Rule
    var mActivityRule: IntentsTestRule<DigitalPDPPulsaActivity> = object: IntentsTestRule<DigitalPDPPulsaActivity>(DigitalPDPPulsaActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            return RouteManager.getIntent(targetContext, getApplink())
        }

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(DigitalPDPPulsaMockConfig())
        }
    }

    @Before
    fun setUp() {

    }

    protected fun clientNumberWidget_typeNumber(number: String) {
        onView(withId(com.tokopedia.unifycomponents.R.id.text_field_input))
            .perform(typeText(number))
    }

    protected fun clientNumberWidget_clickClearIcon() {
        onView(withId(R.id.text_field_icon_close)).perform(click())
    }

    abstract fun getApplink(): String
}