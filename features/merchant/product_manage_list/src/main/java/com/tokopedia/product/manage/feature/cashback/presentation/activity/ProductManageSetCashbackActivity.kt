package com.tokopedia.product.manage.feature.cashback.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment

class ProductManageSetCashbackActivity: BaseSimpleActivity() {

    private var cacheManagerId: String = ""

    override fun getNewFragment(): Fragment? {
        return ProductManageSetCashbackFragment.createInstance(cacheManagerId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cacheManagerId = intent.getStringExtra(ProductManageSetCashbackFragment.SET_CASHBACK_CACHE_MANAGER_KEY)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

}