package com.tokopedia.logisticcart.test

import android.content.Intent
import android.view.View
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.logisticcart.ShippingWidgetCheckoutActivity
import com.tokopedia.logisticcart.dummy.ShippingWidgetDummyType
import com.tokopedia.logisticcart.interceptor.ShippingWidgetInterceptor
import com.tokopedia.logisticcart.robot.shippingWidget
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
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
    fun normalRatesTest() {
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
            assertOptionalInsurance(2000.0, false)
        }

        Thread.sleep(2000)
    }

    @Test
    fun bebasOngkirTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.BEBAS_ONGKIR.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertBebasOngkirShippingVisible()
            assertBebasOngkirShippingCodLabel("", View.GONE)
            assertBebasOngkirShippingTitle("(Rp0)")
            assertBebasOngkirShippingLogoLabel(View.VISIBLE)
            assertBebasOngkirShippingEta("Estimasi tiba 2-4 Feb", View.VISIBLE)
            assertOptionalInsurance(2000.0, true)
        }

        Thread.sleep(2000)
    }

    @Test
    fun now2HourWithPromoTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.NOW_2_HOUR.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertNow2HourShippingVisible()
            assertNow2HourShippingTitle("2 Jam Tiba (Rp0)")
            assertNow2HourShippingDescription("", View.GONE)
            assertOptionalInsurance(2000.0, true)
        }

        Thread.sleep(2000)
    }

    @Test
    fun schellyWithRatesTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.SCHELLY_WITH_RATES.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertSchellyShippingVisible()
        }

        Thread.sleep(2000)
    }

    @Test
    fun unavailableCourierTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.UNAVAILABLE_COURIER.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertUnavailableCourierLayoutVisible()
        }

        Thread.sleep(2000)
    }

    @Test
    fun errorPinpointTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.ERROR_PINPOINT.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertErrorPinpointVisible()
        }

        Thread.sleep(2000)
    }

    @Test
    fun loadingTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.LOADING.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertLoadingVisible()
        }

        Thread.sleep(2000)
    }

    @Test
    fun safErrorTest() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.SAF_ERROR.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            assertSafErrorLayoutVisible()
        }

        Thread.sleep(2000)
    }

    @Test
    fun allShippingState() {
        val intent = Intent(context, ShippingWidgetCheckoutActivity::class.java).apply {
            putExtra("WIDGET_UI_MODEL_KEY", ShippingWidgetDummyType.INITIAL_STATE.name)
        }
        activityRule.launchActivity(intent)

        val widget =
            activityRule.activity.findViewById<ShippingCheckoutRevampWidget>(logisticcarttestR.id.shipping_checkout_widget)
        shippingWidget(widget) {
            clickShippingWidget("Pilih Pengiriman")
            chooseShipment("Reguler (Rp10.000 - Rp11.500)")
            assertNormalShippingVisible()
            assertNormalShippingCodLabel("", View.GONE)
            assertNormalShippingEta("Estimasi tiba 2 - 4 Feb")
            assertNormalShippingCourier("Kurir Rekomendasi (Rp11.500)")
            assertNormalShippingTitle("Reguler")
            assertMustInsurance(300.0)

            // todo click courier
            clickShippingWidget("Reguler")
            chooseShipment("Instant 3 Jam (Rp103.400)")
            assertWhitelabelShippingVisible()
            assertWhitelabelShippingTitle("Instant 3 Jam (Rp103.400)")
            assertWhitelabelShippingEta("Estimasi tiba hari ini - besok, maks. 09:00 WIB")
            assertMustInsurance(800.0)

            clickShippingWidget("Instant 3 Jam (Rp103.400)")
            chooseShipment("Estimasi tiba besok - 15 Sep (Rp0)")
            assertBebasOngkirShippingVisible()
            assertBebasOngkirShippingCodLabel("", View.GONE)
            assertBebasOngkirShippingTitle("Bebas Ongkir (Rp0)")
            assertBebasOngkirShippingLogoLabel(View.GONE)
            assertBebasOngkirShippingEta("Estimasi tiba besok - 15 Sep", View.VISIBLE)
            assertMustInsurance(300.0)
        }
    }

    @Before
    fun setup() {
        ShippingWidgetInterceptor.setupGraphqlMockResponse(context)
        ShippingWidgetInterceptor.resetAllCustomResponse()
    }

    @After
    fun tearDown() {
        if (activityRule.activity?.isDestroyed == false) activityRule.finishActivity()
    }
}
