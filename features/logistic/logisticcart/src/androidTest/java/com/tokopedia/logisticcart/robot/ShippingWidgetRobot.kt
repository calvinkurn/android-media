package com.tokopedia.logisticcart.robot

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.logisticcart.shipping.features.shippingwidget.ShippingCheckoutRevampWidget
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import junit.framework.TestCase.assertEquals

fun shippingWidget(widget: ShippingCheckoutRevampWidget, func: ShippingWidgetRobot.() -> Unit) {
    ShippingWidgetRobot(widget).apply(func)
}

class ShippingWidgetRobot(private val widget: ShippingCheckoutRevampWidget) {

    fun assertNormalShippingVisible() {
        assertEquals(View.VISIBLE, widget.binding?.layoutStateHasSelectedNormalShipping?.visibility)
    }

    fun assertNormalShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingDuration?.text?.toString(), title)
    }

    fun assertNormalShippingCourier(courier: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingCourier?.text?.toString(), courier)
    }

    fun assertNormalShippingEta(eta: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingPriceOrDuration?.text?.toString(), eta)
    }

    fun assertNormalShippingCourierPrice(price: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingPriceOrDuration?.text?.toString(), price)
    }

    fun assertNormalShippingCodLabel(text: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.lblCodAvailable?.visibility, visibility)
        if (visibility == View.VISIBLE) {
            assertEquals(widget.binding?.lblCodAvailable?.text?.toString(), text)
        }
    }

    fun assertWhitelabelShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateHasSelectedWhitelabelShipping?.visibility
        )
    }

    fun assertWhitelabelShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedWhitelabelShipping?.text.toString(), title)
    }

    fun assertWhitelabelShippingEta(text: CharSequence) {
        assertEquals(widget.binding?.labelWhitelabelShippingEta?.text?.toString(), text)
    }

    fun assertBebasOngkirShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateHasSelectedFreeShipping?.visibility
        )
    }

    fun assertBebasOngkirShippingTitle(title: CharSequence) {
        assertEquals(title, widget.binding?.labelSelectedFreeShipping?.text?.toString())
    }

    fun assertBebasOngkirShippingEta(eta: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.labelFreeShippingEta?.text?.toString(), eta)
        assertEquals(
            visibility,
            widget.binding?.labelFreeShippingEta?.visibility
        )
    }

    fun assertBebasOngkirShippingCodLabel(text: CharSequence, visibility: Int) {
        if (visibility == View.VISIBLE) {
            assertEquals(widget.binding?.lblCodFreeShipping?.text?.toString(), text)
        }
        assertEquals(widget.binding?.lblCodFreeShipping?.visibility, visibility)
    }

    fun assertBebasOngkirShippingLogoLabel(visibility: Int) {
        assertEquals(widget.binding?.imgLogoFreeShipping?.visibility, visibility)
    }

    fun assertNow2HourShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateHasSelectedSingleShipping?.visibility
        )
    }

    fun assertNow2HourShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedSingleShippingTitle?.text?.toString(), title)
    }

    fun assertNow2HourShippingDescription(description: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.labelSingleShippingMessage?.visibility, visibility)
        if (visibility == View.VISIBLE) {
            assertEquals(widget.binding?.labelSingleShippingMessage?.text?.toString(), description)
        }
    }

    fun assertSchellyShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.shippingNowWidget?.visibility
        )
    }

    fun assertErrorPinpointVisible() {
        assertEquals(widget.binding?.layoutStateHasSelectedSingleShipping?.visibility, View.VISIBLE)
        assertEquals(widget.binding?.labelSingleShippingEta?.text?.toString(), "Pengiriman & estimasi tiba akan tampil di sini. Atur")
        assertEquals(widget.binding?.labelSelectedSingleShippingTitle?.text?.toString(), "Atur pinpoint alamatmu dulu, ya.")
    }

    fun assertUnavailableCourierLayoutVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateFailedShipping?.visibility
        )
        assertEquals("Pengiriman gagal ditampilkan", widget.binding?.labelFailedShippingTitle?.text)
        assertEquals(View.VISIBLE, widget.binding?.iconReloadShipping?.visibility)
    }

    fun assertEmptyCourierLayoutVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateNoSelectedShipping?.visibility
        )
    }

    fun assertLoadingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.purchasePlatformPartialShimmeringList?.root?.visibility
        )
    }

    fun assertSafErrorLayoutVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateHasErrorShipping?.visibility
        )

        assertEquals(
            "Pengiriman tidak tersedia",
            widget.binding?.labelErrorShippingTitle?.text
        )
    }

    fun assertMustInsurance(price: Double) {
        assertEquals(widget.binding?.layoutShipmentInsurance?.visibility, View.VISIBLE)
        assertEquals(
            widget.binding?.tvInsuranceTitle?.text?.toString(),
            "Dilindungi Asuransi Pengiriman (${
            convertPriceValueToIdrFormat(
                price,
                false
            ).removeDecimalSuffix()
            })"
        )
        assertEquals(widget.binding?.checkboxInsurance?.visibility, View.GONE)
    }

    fun assertNoneInsurance() {
        assertEquals(widget.binding?.layoutShipmentInsurance?.visibility, View.GONE)
    }

    fun assertOptionalInsurance(price: Double, isChecked: Boolean) {
        assertEquals(widget.binding?.layoutShipmentInsurance?.visibility, View.VISIBLE)
        assertEquals(
            widget.binding?.tvInsuranceTitle?.text?.toString(),
            "Pakai Asuransi Pengiriman (${
            convertPriceValueToIdrFormat(
                price,
                false
            ).removeDecimalSuffix()
            })"
        )
        assertEquals(widget.binding?.checkboxInsurance?.visibility, View.VISIBLE)
        assertEquals(widget.binding?.checkboxInsurance?.isChecked, isChecked)
    }

    fun assertInitialStateVisible() {
        assertEquals(View.VISIBLE, widget.binding?.labelChooseShipping?.visibility)
        assertEquals("Pilih Pengiriman", widget.binding?.labelChooseShipping?.text)
    }

    fun clickShippingWidget(text: String) {
        onView(withText(text)).perform(click())
        Thread.sleep(2000)
    }

    fun chooseShipment(service: String) {
        onView(withText(service)).perform(click())
        Thread.sleep(2000)
    }
}
