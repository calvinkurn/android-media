package com.tokopedia.shopdiscount.manage.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.FragmentRouter
import javax.inject.Inject

class ProductManageActivity : AppCompatActivity() {

    @Inject
    lateinit var router : FragmentRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_product_manage)
        displayFragment()
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun displayFragment() {
        val fragment = ProductManageFragment.newInstance()
        router.replace(supportFragmentManager, R.id.container, fragment)
    }

}