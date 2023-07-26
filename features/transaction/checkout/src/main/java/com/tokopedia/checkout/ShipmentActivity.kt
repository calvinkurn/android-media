package com.tokopedia.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.checkout.revamp.view.CheckoutFragment
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.telemetry.ITelemetryActivity

class ShipmentActivity :
    BaseCheckoutActivity(),
    ITelemetryActivity {
    private var shipmentFragment: ShipmentFragment? = null
    private var checkoutFragment: CheckoutFragment? = null

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
        val isPlusSelected = intent.data?.getBooleanQueryParameter(CartConstant.CHECKOUT_IS_PLUS_SELECTED, false) ?: false
        val isOneClickShipment = intent.getBooleanExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, false)
        val pageSource = intent.getStringExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE)
            ?: CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
        val bundle = intent.extras
        checkoutFragment = CheckoutFragment.newInstance(isOneClickShipment, leasingId, pageSource, isPlusSelected, bundle)
        return checkoutFragment
    }

    override fun onBackPressed() {
        if (shipmentFragment != null) {
            shipmentFragment?.onBackPressed()
        } else if (checkoutFragment != null) {
            checkoutFragment?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun getTelemetrySectionName() = "checkout"
}
