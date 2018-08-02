package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.price.ProductEditCategoryFragment
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATALOG
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY

class ProductEditCategoryActivity : BaseSimpleActivity(){

    companion object {
        fun createIntent(context: Context, productCategory: ProductCategory, productCatalog: ProductCatalog): Intent {
            return Intent(context, ProductEditCategoryActivity::class.java)
                    .putExtra(EXTRA_CATALOG, productCatalog)
                    .putExtra(EXTRA_CATEGORY, productCategory)
        }
    }

    override fun getNewFragment(): Fragment{
        return ProductEditCategoryFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
