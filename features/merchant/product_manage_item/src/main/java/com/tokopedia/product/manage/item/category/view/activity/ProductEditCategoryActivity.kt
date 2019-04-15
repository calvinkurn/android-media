package com.tokopedia.product.manage.item.category.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.category.view.fragment.ProductEditCategoryFragment
import com.tokopedia.product.manage.item.category.view.model.ProductCategory
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditFragment.Companion.EXTRA_CATEGORY_LOCKED
import com.tokopedia.product.manage.item.utils.ProductEditItemComponentInstance

class ProductEditCategoryActivity : BaseSimpleActivity(), HasComponent<ProductComponent>{
    private var productName: String = ""
    private var productCatalog: ProductCatalog? = null
    private var productCategory: ProductCategory? = null
    private var isCategoryLocked: Boolean = false

    override fun getComponent(): ProductComponent = ProductEditItemComponentInstance.getComponent(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        productName = intent.getStringExtra(EXTRA_PRODUCT_NAME) ?: ""
        productCatalog = intent.getParcelableExtra(EXTRA_CATALOG)
        productCategory = intent.getParcelableExtra(EXTRA_CATEGORY)
        isCategoryLocked = intent.getBooleanExtra(EXTRA_CATEGORY_LOCKED, false)
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment = ProductEditCategoryFragment
            .createInstance(productName, productCategory, productCatalog, isCategoryLocked)

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu

    companion object {
        private const val EXTRA_PRODUCT_NAME = "extra_product_name"
        private const val EXTRA_CATALOG = "extra_catalog"
        private const val EXTRA_CATEGORY = "extra_category"

        fun createIntent(context: Context, productName: String?, category: ProductCategory?, catalog: ProductCatalog?, isCategoryLocked : Boolean) =
                Intent(context, ProductEditCategoryActivity::class.java).apply {
                    putExtra(EXTRA_PRODUCT_NAME, productName)
                    putExtra(EXTRA_CATALOG, catalog)
                    putExtra(EXTRA_CATEGORY, category)
                    putExtra(EXTRA_CATEGORY_LOCKED, isCategoryLocked)
                }
    }
}
