package com.tokopedia.checkout

import android.app.Activity
import android.os.Bundle
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.tokopedia.checkout.old.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant

class ShipmentActivity : BaseCheckoutActivity() {
    private var shipmentFragment: ShipmentFragment? = null
    private var oldShipmentFragment: com.tokopedia.checkout.old.view.ShipmentFragment? = null

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
        shipmentFragment = ShipmentFragment.newInstance(isOneClickShipment, leasingId, bundle)
        return shipmentFragment
    }

    @Keep
    fun isBundleCheckout(): Boolean {
        return true
    }

    override fun onBackPressed() {
        if (shipmentFragment != null) {
            shipmentFragment?.onBackPressed()
            setResult(shipmentFragment?.resultCode ?: Activity.RESULT_CANCELED)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}