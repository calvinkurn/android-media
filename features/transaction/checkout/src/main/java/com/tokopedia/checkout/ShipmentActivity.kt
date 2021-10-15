package com.tokopedia.checkout

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.checkout.bundle.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.utils.Switch

class ShipmentActivity : BaseCheckoutActivity() {
    private var shipmentFragment: ShipmentFragment? = null
    private var oldShipmentFragment: com.tokopedia.checkout.old.view.ShipmentFragment? = null

    override fun setupBundlePass(extras: Bundle?) {
        // No-op
    }

    override fun initView() {
        // No-op
    }

    override fun setupFragment(savedInstance: Bundle?) {
        inflateFragment()
    }

    override fun getNewFragment(): Fragment? {
        val leasingId = intent.data?.getQueryParameter(CartConstant.CHECKOUT_LEASING_ID) ?: ""
        val isOneClickShipment = intent.getBooleanExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, false)
        val pageSource = intent.getStringExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE)
                ?: CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
        val bundle = intent.extras
        val isBundleToggleOn = Switch.isBundleToggleOn(this)
        if (isBundleToggleOn) {
            shipmentFragment = ShipmentFragment.newInstance(isOneClickShipment, leasingId, pageSource, bundle)
            return shipmentFragment
        } else {
            oldShipmentFragment = com.tokopedia.checkout.old.view.ShipmentFragment.newInstance(isOneClickShipment, leasingId, pageSource, bundle)
            return oldShipmentFragment
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