package com.tokopedia.shopdiscount.select.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.constant.ZERO

class SelectProductActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID = "discount-status-id"

        @JvmStatic
        fun start(context: Context, discountStatusId: Int) {
            val starter = Intent(context, SelectProductActivity::class.java)
            starter.putExtra(BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID, discountStatusId)
            context.startActivity(starter)
        }
    }

    private val discountStatusId by lazy {
        intent.getIntExtra(
            BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID,
            ZERO
        ).orZero()
    }

    override fun getLayoutRes() = R.layout.activity_select_product
    override fun getNewFragment() = SelectProductFragment.newInstance(discountStatusId)
    override fun getParentViewResourceID() = R.id.container

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_select_product)
    }
}