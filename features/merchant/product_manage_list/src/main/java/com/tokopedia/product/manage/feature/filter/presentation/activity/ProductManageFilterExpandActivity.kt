package com.tokopedia.product.manage.feature.filter.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterExpandChecklistFragment
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterExpandSelectFragment
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.CATEGORIES_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ETALASE_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.OTHER_FILTER_CACHE_MANAGER_KEY
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.SORT_CACHE_MANAGER_KEY

class ProductManageFilterExpandActivity: BaseSimpleActivity() {

    private var cacheManagerId: String = ""
    private var fragmentflag = ""

    override fun getNewFragment(): Fragment? {
        return when(fragmentflag) {
            SORT_CACHE_MANAGER_KEY -> {
                ProductManageFilterExpandSelectFragment.createInstance(SORT_CACHE_MANAGER_KEY, cacheManagerId)
            }
            ETALASE_CACHE_MANAGER_KEY -> {
                ProductManageFilterExpandSelectFragment.createInstance(ETALASE_CACHE_MANAGER_KEY, cacheManagerId)
            }
            CATEGORIES_CACHE_MANAGER_KEY -> {
                ProductManageFilterExpandChecklistFragment.createInstance(CATEGORIES_CACHE_MANAGER_KEY, cacheManagerId)
            }
            OTHER_FILTER_CACHE_MANAGER_KEY -> {
                ProductManageFilterExpandChecklistFragment.createInstance(OTHER_FILTER_CACHE_MANAGER_KEY, cacheManagerId)
            }
            else -> super.getFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        cacheManagerId = intent.getStringExtra(ProductManageFilterFragment.CACHE_MANAGER_KEY)
        fragmentflag = intent.getStringExtra(ProductManageFilterFragment.ACTIVITY_EXPAND_FLAG)
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
}