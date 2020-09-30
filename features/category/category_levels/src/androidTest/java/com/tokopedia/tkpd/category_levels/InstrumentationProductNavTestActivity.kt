package com.tokopedia.tkpd.category_levels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tokopedia.categorylevels.test.R
import com.tokopedia.categorylevels.view.fragment.ProductNavFragment

class InstrumentationProductNavTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_nav_test)
        val fragment: Fragment = ProductNavFragment()
        val fragmentTransaction = supportFragmentManager
                .beginTransaction()
        fragmentTransaction
                .replace(R.id.container_product_nav, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}