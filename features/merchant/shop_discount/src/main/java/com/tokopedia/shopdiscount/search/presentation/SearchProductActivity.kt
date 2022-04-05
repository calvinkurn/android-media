package com.tokopedia.shopdiscount.search.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.manage.presentation.container.ProductManageFragment
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import javax.inject.Inject

class SearchProductActivity : AppCompatActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, SearchProductActivity::class.java)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var router : FragmentRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_search_product)
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