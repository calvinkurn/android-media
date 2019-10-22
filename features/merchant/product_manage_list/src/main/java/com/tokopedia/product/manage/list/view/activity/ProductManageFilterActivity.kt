package com.tokopedia.product.manage.list.view.activity

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_FILTER_SELECTED
import com.tokopedia.product.manage.list.data.model.ProductManageFilterModel
import com.tokopedia.product.manage.list.view.fragment.ProductManageFilterFragment

class ProductManageFilterActivity: BaseSimpleActivity() {

    companion object{
        fun createIntent(context: Context, productManageFilterModel: ProductManageFilterModel) = Intent(context, ProductManageFilterActivity::class.java).apply {
            putExtra(EXTRA_FILTER_SELECTED, productManageFilterModel)
        }
    }

    override fun getNewFragment(): Fragment? {
        return ProductManageFilterFragment.createInstance(intent.getParcelableExtra(EXTRA_FILTER_SELECTED))
    }

    override fun isShowCloseButton(): Boolean = true

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter_product, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.reset_menu) {
            (fragment as ProductManageFilterFragment).onResetFilter()
        }
        return super.onOptionsItemSelected(item)
    }

}