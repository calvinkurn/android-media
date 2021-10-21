package com.tokopedia.checkout.old

import android.os.Bundle
import com.tokopedia.checkout.old.view.ShipmentFragment

class OldShipmentFragment: ShipmentFragment() {

    companion object {
        fun newInstance(isOneClickShipment: Boolean,
                        leasingId: String?,
                        pageSource: String?,
                        bundle: Bundle?): OldShipmentFragment {
            var bundle = bundle
            if (bundle == null) {
                bundle = Bundle()
            }
            bundle.putString(ARG_CHECKOUT_LEASING_ID, leasingId)
            if (leasingId != null && !leasingId.isEmpty()) {
                bundle.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, true)
            } else {
                bundle.putBoolean(ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment)
            }
            bundle.putString(ARG_CHECKOUT_PAGE_SOURCE, pageSource)
            val shipmentFragment = OldShipmentFragment()
            shipmentFragment.arguments = bundle
            return shipmentFragment
        }
    }

    override fun isBundleToggleChanged(): Boolean {
        return false
    }
}