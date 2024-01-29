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

    fun assertNormalShippingTitle(title: CharSequence) {
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

    fun assertWhitelabelShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateHasSelectedWhitelabelShipping?.visibility
        )
    }

    fun assertWhitelabelShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedWhitelabelShipping?.text, title)
    }

    fun assertWhitelabelShippingEta(text: CharSequence) {
        assertEquals(widget.binding?.labelWhitelabelShippingEta?.text, text)
    }

    fun assertBebasOngkirShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateHasSelectedFreeShipping?.visibility
        )
    }

    fun assertBebasOngkirShippingTitle(title: CharSequence) {
        assertEquals(widget.binding?.labelSelectedFreeShipping?.text, title)
    }

    fun assertBebasOngkirShippingEta(eta: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.labelFreeShippingEta?.text, eta)
        assertEquals(
            visibility,
            widget.binding?.labelFreeShippingEta?.visibility
        )
    }

    fun assertBebasOngkirShippingCodLabel(text: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.lblCodFreeShipping?.visibility, visibility)
        assertEquals(widget.binding?.lblCodFreeShipping?.text, text)
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
        assertEquals(widget.binding?.labelSelectedSingleShippingTitle?.text, title)
    }

    fun assertNow2HourShippingDescription(description: CharSequence, visibility: Int) {
        assertEquals(widget.binding?.labelSelectedSingleShippingTitle?.text, description)
    }

    fun assertSchellyShippingVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.shippingNowWidget?.visibility
        )
    }

    fun assertErrorPinpointVisible(text: CharSequence) {
        assertEquals(widget.binding?.layoutStateHasSelectedSingleShipping?.visibility, View.VISIBLE)
        assertEquals(widget.binding?.labelSingleShippingEta?.text, text)
    }

    fun assertUnavailableCourierLayoutVisible() {
        assertEquals(
            View.VISIBLE,
            widget.binding?.layoutStateFailedShipping?.visibility
        )
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
