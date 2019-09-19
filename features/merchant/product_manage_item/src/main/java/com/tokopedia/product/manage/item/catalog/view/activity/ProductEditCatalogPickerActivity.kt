package com.tokopedia.product.manage.item.catalog.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.catalog.view.fragment.ProductEditCatalogPickerFragment
import com.tokopedia.product.manage.item.catalog.view.model.ProductCatalog
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.utils.ProductEditItemComponentInstance

class ProductEditCatalogPickerActivity : BaseSimpleActivity(), HasComponent<ProductComponent> {
    private var productName: String = ""
    private var categoryId = -1L
    private var choosenCatalog: ProductCatalog = ProductCatalog()

    companion object {
        private const val EXTRA_PRODUCT_NAME = "product_name"
        private const val EXTRA_CATEGORY_ID = "category_id"
        private const val EXTRA_CATALOG = "catalog"

        fun createIntent(context: Context, productName: String, categoryId: Long, choosenCatalog: ProductCatalog) =
                Intent(context, ProductEditCatalogPickerActivity::class.java).apply {
                    putExtra(EXTRA_PRODUCT_NAME, productName)
                    putExtra(EXTRA_CATEGORY_ID, categoryId)
                    putExtra(EXTRA_CATALOG, choosenCatalog)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        productName = intent.getStringExtra(EXTRA_PRODUCT_NAME) ?: ""
        categoryId = intent.getLongExtra(EXTRA_CATEGORY_ID, -1L)
        choosenCatalog = intent.getParcelableExtra(EXTRA_CATALOG)
        super.onCreate(savedInstanceState)
    }

    override fun getComponent() = ProductEditItemComponentInstance.getComponent(application)

    override fun getNewFragment() = ProductEditCatalogPickerFragment
            .createInstance(productName, categoryId, choosenCatalog)

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu
}
