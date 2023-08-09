package com.tokopedia.cart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.cart.R
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.telemetry.ITelemetryActivity

class CartActivity :
    BaseCheckoutActivity(),
    ITelemetryActivity {

    private var fragment: CartRevampFragment? = null
    private var cartId: String? = null
    private var productId: Long = 0L

    override fun getLayoutRes() = R.layout.activity_cart

    override fun getParentViewResourceID() = R.id.parent_view

    override fun setupBundlePass(extras: Bundle?) {
        val productIdStr = intent?.data?.getQueryParameter(APPLINK_PARAM_PRODUCT_ID) ?: ""
        if (productIdStr.isNotBlank()) {
            productId = productIdStr.toLongOrNull() ?: INVALID_PRODUCT_ID
        }
        cartId = extras?.getString(EXTRA_CART_ID)
    }

    override fun initView() {}

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        updateTitle(title.toString())
    }

    override fun onBackPressed() {
        if (fragment != null) {
            fragment?.onBackPressed()
        } else {
            finish()
        }
    }

    override fun setupFragment(savedInstance: Bundle?) {
        inflateFragment()
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(EXTRA_CART_ID, cartId)
        bundle.putLong(EXTRA_PRODUCT_ID, productId)
        bundle.putBoolean(EXTRA_IS_FROM_CART_ACTIVITY, true)
        fragment = CartRevampFragment.newInstance(bundle, "")
        return fragment
    }

    companion object {
        const val EXTRA_CART_ID = "cart_id"
        const val EXTRA_PRODUCT_ID = "product_id"

        const val INVALID_PRODUCT_ID = -1L
        const val APPLINK_PARAM_PRODUCT_ID = "product_id"
        const val EXTRA_IS_FROM_CART_ACTIVITY = "EXTRA_IS_FROM_CART_ACTIVITY"
    }

    override fun getTelemetrySectionName() = "atc"
}
