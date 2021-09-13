package com.tokopedia.checkout

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.checkout.bundle.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.utils.Switch
import timber.log.Timber

class ShipmentActivity : BaseCheckoutActivity() {
    private var shipmentFragment: ShipmentFragment? = null
    private var oldShipmentFragment: com.tokopedia.checkout.old.view.ShipmentFragment? = null

    private var isBundleToggleOn: Boolean? = null

    override fun setupBundlePass(extras: Bundle?) {
        // No-op
    }

    override fun initView() {
        // No-op
    }

    override fun getNewFragment(): Fragment? {
        val leasingId = intent.data?.getQueryParameter(CartConstant.CHECKOUT_LEASING_ID) ?: ""
        val isOneClickShipment = intent.getBooleanExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, false)
        val bundle = intent.extras
        isBundleToggleOn = Switch.isBundleToggleOn()
        if (isBundleToggleOn == true) {
            shipmentFragment = ShipmentFragment.newInstance(isOneClickShipment, leasingId, bundle)
            return shipmentFragment
        } else {
            oldShipmentFragment = com.tokopedia.checkout.old.view.ShipmentFragment.newInstance(isOneClickShipment, leasingId, bundle)
            return oldShipmentFragment
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            if (isBundleToggleOn != null && isBundleToggleOn != Switch.isBundleToggleOn()) {
                recreate()
            }
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    override fun onBackPressed() {
        if (shipmentFragment != null) {
            shipmentFragment?.onBackPressed()
            setResult(shipmentFragment?.resultCode ?: Activity.RESULT_CANCELED)
            finish()
        } else if (oldShipmentFragment != null) {
            oldShipmentFragment?.onBackPressed()
            setResult(oldShipmentFragment?.resultCode ?: Activity.RESULT_CANCELED)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}