package com.tokopedia.product.manage.item.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.item.price.model.ProductCatalog
import com.tokopedia.product.manage.item.price.model.ProductCategory
import com.tokopedia.product.manage.item.price.model.ProductName
import com.tokopedia.product.manage.item.util.ProductEditModuleRouter
import com.tokopedia.product.manage.item.view.fragment.BaseProductAddEditFragment
import com.tokopedia.product.manage.item.view.fragment.ProductAddFragment
import com.tokopedia.product.manage.item.view.fragment.ProductEditFragment
import com.tokopedia.product.manage.item.view.fragment.ProductEditFragment.Companion.EDIT_PRODUCT_ID

class ProductEditActivity : ProductAddActivity(), HasComponent<ProductComponent> {
    override fun getNewFragment(): Fragment {
        val productId = intent.getStringExtra(EDIT_PRODUCT_ID)
        return ProductEditFragment.createInstance(productId)
    }

    override fun getLayoutRes() = R.layout.activity_product_edit_with_menu

    companion object {
        fun createInstance(context: Context?, productId: String ) = Intent(context, ProductEditActivity::class.java).apply {
                putExtra(EDIT_PRODUCT_ID, productId)
            }
    }

    override fun getComponent() = (application as ProductEditModuleRouter).getProductComponent()

    override fun getCancelMessageRes() = R.string.product_draft_dialog_edit_cancel_message
}