package com.tokopedia.shopdiscount.search.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent

class SearchProductActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_NAME = "discount-status-name"
        private const val BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID = "discount-status-id"

        @JvmStatic
        fun start(context: Context, discountStatusName : String, discountStatusId: Int) {
            val starter = Intent(context, SearchProductActivity::class.java)
            starter.putExtra(BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_NAME, discountStatusName)
            starter.putExtra(BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID, discountStatusId)
            context.startActivity(starter)
        }
    }

    private val discountStatusName by lazy {
        intent.getStringExtra(
            BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_NAME
        ).orEmpty()
    }

    private val discountStatusId by lazy {
        intent.getIntExtra(
            BUNDLE_KEY_BUNDLE_DISCOUNT_STATUS_ID,
            0
        ).orZero()
    }

    override fun getLayoutRes() = R.layout.activity_search_product
    override fun getNewFragment() = SearchProductFragment.newInstance(discountStatusName, discountStatusId)
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
        setContentView(R.layout.activity_search_product)
    }
}