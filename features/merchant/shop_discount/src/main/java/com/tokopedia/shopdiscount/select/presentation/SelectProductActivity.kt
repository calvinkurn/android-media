package com.tokopedia.shopdiscount.select.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import javax.inject.Inject

class SelectProductActivity : AppCompatActivity() {

    companion object {
        private const val BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID = "discount-status-id"

        @JvmStatic
        fun start(context: Context, discountStatusId: Int) {
            val starter = Intent(context, SelectProductActivity::class.java)
            starter.putExtra(BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID, discountStatusId)
            context.startActivity(starter)
        }
    }

    @Inject
    lateinit var router: FragmentRouter

    private val discountStatusId by lazy {
        intent.getIntExtra(
            BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID,
            0
        ).orZero()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_select_product)
        displayFragment()
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun displayFragment() {
        val fragment = SelectProductFragment.newInstance(discountStatusId)
        router.replace(supportFragmentManager, R.id.container, fragment)
    }

}