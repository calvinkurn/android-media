package com.tokopedia.product.manage.stub.feature.list.view

import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.view.activity.ProductManageActivity
import com.tokopedia.product.manage.stub.feature.list.di.ProductManageListStubInstance

class ProductManageActivityStub: ProductManageActivity() {

    override fun getComponent(): ProductManageListComponent {
        return ProductManageListStubInstance.getComponent(this)
    }
}