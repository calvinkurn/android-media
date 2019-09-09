package com.tokopedia.product.manage.item.stock.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.stock.view.fragment.ProductEditStockFragment
import com.tokopedia.product.manage.item.stock.view.model.ProductStock
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_IS_STATUS_ADD
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_STOCK

class ProductEditStockActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productStock: ProductStock?, hasVariant: Boolean, isAddStatus: Boolean): Intent {
            return Intent(context, ProductEditStockActivity::class.java)
                    .putExtra(EXTRA_STOCK, productStock)
                    .putExtra(EXTRA_HAS_VARIANT, hasVariant)
                    .putExtra(EXTRA_IS_STATUS_ADD, isAddStatus)
        }
    }

    override fun getNewFragment(): Fragment = ProductEditStockFragment.createInstance()

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu
}
