package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.price.model.ProductCatalog
import com.tokopedia.product.edit.price.model.ProductCategory
import com.tokopedia.product.edit.price.model.ProductName
import com.tokopedia.product.edit.util.ProductEditModuleRouter
import com.tokopedia.product.edit.view.fragment.BaseProductAddEditFragment
import com.tokopedia.product.edit.view.fragment.ProductAddFragment
import com.tokopedia.product.edit.view.fragment.ProductEditFragment
import com.tokopedia.product.edit.view.fragment.ProductEditFragment.Companion.EDIT_PRODUCT_ID

class ProductEditActivity : ProductAddActivity(), HasComponent<ProductComponent> {
    override fun getNewFragment(): Fragment {
        val productId = intent.getStringExtra(EDIT_PRODUCT_ID)
        return ProductEditFragment.createInstance(productId)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    companion object {
        fun createInstance(context: Context?, productId: String ): Intent {
            val intent = Intent(context, ProductEditActivity::class.java)
            intent.putExtra(EDIT_PRODUCT_ID, productId)
            return intent
        }
    }

    override fun getComponent(): ProductComponent {
        return (application as ProductEditModuleRouter).getProductComponent()
    }
}