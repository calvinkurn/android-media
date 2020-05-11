package com.tokopedia.purchase_platform.features.cart.view

import android.os.Bundle

import androidx.fragment.app.Fragment

import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartActivity : BaseCheckoutActivity() {

    private var cartId: String? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_cart
    }

    override fun initInjector() {}

    override fun setupBundlePass(extras: Bundle?) {
        cartId = extras?.getString(EXTRA_CART_ID)
    }

    override fun initView() {}

    override fun setTitle(title: CharSequence) {
        super.setTitle(title)
        updateTitle(title.toString())
    }

    override fun onBackPressed() {
        val currentFragment = currentFragment
        if (currentFragment is ICartListAnalyticsListener) {
            (currentFragment as ICartListAnalyticsListener).sendAnalyticsOnClickBackArrow()
        }
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(EXTRA_CART_ID, cartId)
        return CartFragment.newInstance(bundle, "")
    }

    companion object {
        val EXTRA_CART_ID = "cart_id"
    }

}
