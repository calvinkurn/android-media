package com.tokopedia.purchase_platform.common.feature.checkout

import android.os.Bundle

/**
 * Created by Irfan Khoirul on 08/03/19.
 * Store data needed to load Shipment Form / Checkout Page.
 */
class ShipmentFormRequest(private val deviceId: String) {
    val bundle: Bundle
        get() {
            val bundle = Bundle()
            bundle.putString(EXTRA_DEVICE_ID, deviceId)
            return bundle
        }

    class BundleBuilder {
        private var deviceId: String = ""

        // Add this data for trade in feature
        fun deviceId(deviceId: String): BundleBuilder {
            this.deviceId = deviceId
            return this
        }

        fun build(): ShipmentFormRequest {
            return ShipmentFormRequest(deviceId)
        }
    }

    companion object {
        const val EXTRA_DEVICE_ID = "EXTRA_DEVICE_ID"
    }
}
