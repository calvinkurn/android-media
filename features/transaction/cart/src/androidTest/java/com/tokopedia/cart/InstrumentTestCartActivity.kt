package com.tokopedia.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.cart.test.R
import com.tokopedia.cart.view.CartFragment
import com.tokopedia.purchase_platform.common.constant.CartConstant.REFRESH_CART_AFTER_BACK_FROM_PDP

class InstrumentTestCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_test)

        val bundle = Bundle()
        bundle.putBoolean(REFRESH_CART_AFTER_BACK_FROM_PDP, false)
        val cartFragment = CartFragment.newInstance(bundle, "")
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view_cart, cartFragment, "")
                .commit()
    }

}