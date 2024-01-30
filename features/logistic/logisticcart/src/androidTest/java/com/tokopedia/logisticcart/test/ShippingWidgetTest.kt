package com.tokopedia.logisticcart.test

import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.logisticcart.ShippingWidgetCheckoutActivity
import com.tokopedia.logisticcart.dummy.ShippingWidgetDummyType
import com.tokopedia.logisticcart.robot.shippingWidget
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Rule
import org.junit.Test
import com.tokopedia.logisticcart.test.R as logisticcarttestR

@UiTest
class ShippingWidgetTest {

    @get:Rule
    var activityRule =
        object :
            IntentsTestRule<ShippingWidgetCheckoutActivity>(
                ShippingWidgetCheckoutActivity::class.java,
                false,
                false
            ) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                InstrumentationAuthHelper.loginInstrumentationTestUser1()
            }
        }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun happyFlowTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.NORMAL_FLOW.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertNormalShippingVisible()
            assertNormalShippingCodLabel("", View.GONE)
            assertNormalShippingEta("Estimasi Tiba 1 - 3 Feb")
            assertNormalShippingCourier("Kurir Rekomendasi (Rp11.500)")
            assertNormalShippingTitle("Reguler")
            assertMustInsurance(2000.0)
        }

        Thread.sleep(2000)
    }

    @Test
    fun initialTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.INITIAL_STATE.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertInitialStateVisible()
        }

        Thread.sleep(2000)
    }

    @Test
    fun whitelabelTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.WHITELABEL_FLOW.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertWhitelabelShippingVisible()
            assertWhitelabelShippingTitle("Instan (Rp30.000)")
            assertWhitelabelShippingEta("Estimasi Tiba 1 - 3 Feb")
        }

        Thread.sleep(2000)
    }

    @After
    fun tearDown() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
