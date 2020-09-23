package com.tokopedia.cart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.cart.test.R
import com.tokopedia.cart.view.CartFragment

class InstrumentTestCartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_test)

        val cartFragment = CartFragment.newInstance(Bundle(), "")
        supportFragmentManager.beginTransaction()
                .replace(R.id.parent_view_cart, cartFragment, "")
                .commit()
    }

}