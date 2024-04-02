package com.tokopedia.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.checkout.backup.view.BackupCheckoutFragment
import com.tokopedia.checkout.revamp.view.CheckoutFragment
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.purchase_platform.common.constant.ARGS_LIST_AUTO_APPLY_PROMO
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoExternalAutoApply
import com.tokopedia.purchase_platform.common.revamp.CartCheckoutRevampRollenceManager
import com.tokopedia.telemetry.ITelemetryActivity

open class ShipmentActivity :
    BaseCheckoutActivity(),
    ITelemetryActivity {
    internal var shipmentFragment: ShipmentFragment? = null
    internal var checkoutFragment: CheckoutFragment? = null
    internal var backupCheckoutFragment: BackupCheckoutFragment? = null

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
        val cartCheckoutRevampRollenceManager = CartCheckoutRevampRollenceManager()
        val isRevamp = cartCheckoutRevampRollenceManager.isRevamp()
        val leasingId = intent.data?.getQueryParameter(CartConstant.CHECKOUT_LEASING_ID) ?: ""
        val isPlusSelected = intent.data?.getBooleanQueryParameter(CartConstant.CHECKOUT_IS_PLUS_SELECTED, false) ?: false
        val isOneClickShipment = intent.getBooleanExtra(CheckoutConstant.EXTRA_IS_ONE_CLICK_SHIPMENT, false)
        val pageSource = intent.getStringExtra(CheckoutConstant.EXTRA_CHECKOUT_PAGE_SOURCE)
            ?: CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
        val promos = intent.getParcelableArrayListExtra<PromoExternalAutoApply>(ARGS_LIST_AUTO_APPLY_PROMO) ?: ArrayList()
        val bundle = intent.extras
        val isTradeIn = bundle?.getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "")?.isNotEmpty() ?: false
        if (shouldRedirectToRevamp(isRevamp, isPlusSelected, isOneClickShipment, isTradeIn)) {
            val shouldRedirectBasedOnRc = cartCheckoutRevampRollenceManager.shouldRedirectToNewCheckoutPayment(this)
            if (shouldRedirectToNewCheckoutPayment(shouldRedirectBasedOnRc, isRevamp, isPlusSelected, isOneClickShipment, isTradeIn)) {
                checkoutFragment = CheckoutFragment.newInstance(
                    isOneClickShipment,
                    leasingId,
                    pageSource,
                    isPlusSelected,
                    ArrayList(promos),
                    intent?.data?.getQueryParameter(CheckoutFragment.QUERY_GATEWAY_CODE),
                    intent?.data?.getQueryParameter(CheckoutFragment.QUERY_TENURE_TYPE),
                    intent?.data?.getQueryParameter(CheckoutFragment.QUERY_SOURCE),
                    bundle
                )
                return checkoutFragment
            } else {
                backupCheckoutFragment = BackupCheckoutFragment.newInstance(
                    isOneClickShipment,
                    leasingId,
                    pageSource,
                    isPlusSelected,
                    bundle
                )
                return backupCheckoutFragment
            }
        } else {
            shipmentFragment = ShipmentFragment.newInstance(
                isOneClickShipment,
                leasingId,
                pageSource,
                isPlusSelected,
                bundle
            )
            return shipmentFragment
        }
    }

    private fun shouldRedirectToRevamp(isRevamp: Boolean, isPlusSelected: Boolean, isOneClickShipment: Boolean, isTradeIn: Boolean): Boolean {
        return isRevamp && !isTradeIn
    }

    private fun shouldRedirectToNewCheckoutPayment(shouldRedirectBasedOnRc: Boolean, isRevamp: Boolean, isPlusSelected: Boolean, isOneClickShipment: Boolean, isTradeIn: Boolean): Boolean {
        return shouldRedirectBasedOnRc
    }

    override fun onBackPressed() {
        if (shipmentFragment != null) {
            shipmentFragment?.onBackPressed()
        } else if (checkoutFragment != null) {
            checkoutFragment?.onBackPressed()
        } else if (backupCheckoutFragment != null) {
            backupCheckoutFragment?.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun getTelemetrySectionName() = "checkout"
}
