package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.ProductEditStockFragment
import com.tokopedia.product.edit.price.model.ProductStock
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_HAS_VARIANT
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_IS_STATUS_ADD
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_STOCK

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
