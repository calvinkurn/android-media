package com.tokopedia.cart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.cart.R
import com.tokopedia.purchase_platform.common.base.BaseCheckoutActivity

/**
 * @author anggaprasetiyo on 18/01/18.
 */

class CartActivity : BaseCheckoutActivity() {

    lateinit var fragment: CartFragment
    private var cartId: String? = null
    private var productId: Long = 0L

    override fun getLayoutRes(): Int {
        return R.layout.activity_cart
    }

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
        if (::fragment.isInitialized) {
            fragment.onBackPressed()
        } else {
            finish()
        }
    }

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        bundle.putString(EXTRA_CART_ID, cartId)
        bundle.putLong(EXTRA_PRODUCT_ID, productId)
        fragment = CartFragment.newInstance(bundle, "")

        return fragment
    }

    companion object {
        const val EXTRA_CART_ID = "cart_id"
        const val EXTRA_PRODUCT_ID = "product_id"

        const val INVALID_PRODUCT_ID = -1L
        const val APPLINK_PARAM_PRODUCT_ID = "product_id"
    }

}
