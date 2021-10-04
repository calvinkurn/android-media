package com.tokopedia.checkout.old

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.checkout.old.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant

class OldShipmentActivity : BaseCheckoutActivity() {
    private var oldShipmentFragment: ShipmentFragment? = null

    override fun setupBundlePass(extras: Bundle?) {
        // No-op
    }

    override fun initView() {
        // No-op
    }

    override fun getNewFragment(): Fragment? {
        val leasingId = intent.data?.getQueryParameter(CartConstant.CHECKOUT_LEASING_ID) ?: ""
        val isOneClickShipment = intent.getBooleanExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, false)
        val pageSource = intent.getStringExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE) ?: CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
        val bundle = intent.extras
        oldShipmentFragment = ShipmentFragment.newInstance(isOneClickShipment, leasingId, pageSource, bundle)
        return oldShipmentFragment
    }

    override fun onBackPressed() {
        if (oldShipmentFragment != null) {
            oldShipmentFragment?.onBackPressed()
            setResult(oldShipmentFragment?.resultCode ?: Activity.RESULT_CANCELED)
            finish()
        } else {
            super.onBackPressed()
        }
    }
}