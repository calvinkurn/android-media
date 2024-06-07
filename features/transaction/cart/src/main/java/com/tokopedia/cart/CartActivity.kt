package com.tokopedia.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.cart.view.CartRevampFragment
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity
import com.tokopedia.telemetry.ITelemetryActivity

class CartActivity :
    BaseCheckoutActivity(),
    ITelemetryActivity,
    AppLogInterface,
    IAdsLog {

    private var revampFragment: CartRevampFragment? = null
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
        if (revampFragment != null) {
            revampFragment?.onBackPressed()
        } else {
            finish()
        }
    }

    override fun setupFragment(savedInstance: Bundle?) {
        inflateFragment()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putString(EXTRA_CART_ID, cartId)
        bundle.putLong(EXTRA_PRODUCT_ID, productId)
        bundle.putBoolean(EXTRA_IS_FROM_CART_ACTIVITY, true)
        return CartRevampFragment.newInstance(bundle, "")
    }

    companion object {
        const val EXTRA_CART_ID = "cart_id"
        const val EXTRA_PRODUCT_ID = "product_id"

        const val INVALID_PRODUCT_ID = -1L
        const val APPLINK_PARAM_PRODUCT_ID = "product_id"
        const val EXTRA_IS_FROM_CART_ACTIVITY = "EXTRA_IS_FROM_CART_ACTIVITY"
    }

    override fun getTelemetrySectionName() = "atc"

    override fun getPageName(): String {
        return PageName.CART
    }

    override fun getAdsPageName(): String {
        return PageName.CART
    }

    override fun isEnterFromWhitelisted(): Boolean {
        return true
    }
}
