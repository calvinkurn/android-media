package com.tokopedia.product.edit.view.activity

import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.price.ProductAddNameCategoryFragment
import com.tokopedia.product.edit.util.ProductEditModuleRouter

class ProductAddNameCategoryActivity : BaseSimpleActivity(), HasComponent<ProductComponent>{
    override fun getComponent(): ProductComponent {
        return (application as ProductEditModuleRouter).productComponent
    }

    override fun getNewFragment(): Fragment{
        return ProductAddNameCategoryFragment.createInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
