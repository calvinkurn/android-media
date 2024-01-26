package com.tokopedia.logisticcart.robot

import android.view.View
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

    fun assertWhitelabelShippingVisible() {
        assertEquals(View.VISIBLE, widget.binding?.layoutStateHasSelectedWhitelabelShipping?.visibility)
    }

    fun assertNormalShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingDuration?.text, title)
    }

    fun assertWhitelabelShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingDuration?.text, title)
    }

    fun assertNormalShippingCourier(courier: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingCourier?.text, courier)
    }

    fun assertNormalShippingEta(eta: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingPriceOrDuration?.text, eta)
    }

    fun assertNormalShippingCourierPrice(price: CharSequence) {
        assertEquals(widget.binding?.labelSelectedShippingPriceOrDuration?.text, price)
    }

    fun assertNormalShippingCodLabel(text: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.lblCodAvailable?.visibility, visibility)
        assertEquals(widget.binding?.lblCodAvailable?.text, text)
    }

    fun assertMustInsurance(price: Double) {
        assertEquals(widget.binding?.layoutShipmentInsurance?.visibility, View.VISIBLE)
        assertEquals(
            widget.binding?.tvInsuranceTitle?.text,
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
            widget.binding?.tvInsuranceTitle?.text,
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
}
