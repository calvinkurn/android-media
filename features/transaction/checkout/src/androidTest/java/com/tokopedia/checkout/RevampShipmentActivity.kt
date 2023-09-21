package com.tokopedia.checkout

import androidx.fragment.app.Fragment
import com.tokopedia.checkout.revamp.view.CheckoutFragment
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant

class RevampShipmentActivity : ShipmentActivity() {

    override fun getNewFragment(): Fragment? {
        val leasingId = intent.data?.getQueryParameter(CartConstant.CHECKOUT_LEASING_ID) ?: ""
        val isPlusSelected = intent.data?.getBooleanQueryParameter(CartConstant.CHECKOUT_IS_PLUS_SELECTED, false) ?: false
        val isOneClickShipment = intent.getBooleanExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, false)
        val pageSource = intent.getStringExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE)
            ?: CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
        val bundle = intent.extras
        checkoutFragment = CheckoutFragment.newInstance(
            isOneClickShipment,
            leasingId,
            pageSource,
            isPlusSelected,
            bundle
        )
        return checkoutFragment
    }
}
