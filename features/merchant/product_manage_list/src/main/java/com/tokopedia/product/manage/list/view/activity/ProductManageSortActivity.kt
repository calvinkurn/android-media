package com.tokopedia.product.manage.list.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_SORT_SELECTED
import com.tokopedia.product.manage.list.constant.option.SortProductOption
import com.tokopedia.product.manage.list.view.fragment.ProductManageSortFragment

class ProductManageSortActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context, @SortProductOption sortProductOption: String) = Intent(context, ProductManageSortActivity::class.java).apply {
            putExtra(EXTRA_SORT_SELECTED, sortProductOption)
        }
    }

    override fun getNewFragment(): Fragment? = ProductManageSortFragment.createInstance(intent.getStringExtra(EXTRA_SORT_SELECTED))
    override fun isShowCloseButton(): Boolean = true
}