package com.tokopedia.product.edit.view.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.util.ProductEditModuleRouter

abstract class BaseProductAddEditActivity : BaseSimpleActivity(), HasComponent<ProductComponent>{


    override fun getComponent(): ProductComponent {
        return (application as ProductEditModuleRouter).productComponent
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }
}
