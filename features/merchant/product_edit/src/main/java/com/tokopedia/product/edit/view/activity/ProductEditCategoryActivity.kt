package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.price.ProductEditCategoryFragment
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.util.ProductEditModuleRouter

class ProductEditCategoryActivity : BaseSimpleActivity(), HasComponent<ProductComponent>{
    private var productName: String = ""
    private var productCatalog: ProductCatalog? = null
    private var productCategory: ProductCategory? = null

    override fun getComponent(): ProductComponent = (application as ProductEditModuleRouter).getProductComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        productName = intent.getStringExtra(EXTRA_PRODUCT_NAME) ?: ""
        productCatalog = intent.getParcelableExtra(EXTRA_CATALOG)
        productCategory = intent.getParcelableExtra(EXTRA_CATEGORY)
        super.onCreate(savedInstanceState)

    }

    override fun getNewFragment(): Fragment{
        return ProductEditCategoryFragment.createInstance(productName, productCategory, productCatalog)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        private const val EXTRA_PRODUCT_NAME = "extra_product_name"
        private const val EXTRA_CATALOG = "extra_catalog"
        private const val EXTRA_CATEGORY = "extra_category"

        fun createIntent(context: Context, productName: String?, category: ProductCategory?, catalog: ProductCatalog?) =
                Intent(context, ProductEditCategoryActivity::class.java).apply {
                    putExtra(EXTRA_PRODUCT_NAME, productName)
                    putExtra(EXTRA_CATALOG, catalog)
                    putExtra(EXTRA_CATEGORY, category)
                }
    }
}
