package com.tokopedia.product.manage.item.stock.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.stock.view.fragment.ProductBulkEditStockFragment

class ProductBulkEditStockActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? = ProductBulkEditStockFragment.createInstance()
    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ProductBulkEditStockActivity::class.java)
        }
    }
}
