package com.tokopedia.product.manage.stub.feature.list.di

import android.content.Context
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.di.ProductManageListModule
import com.tokopedia.product.manage.stub.feature.ProductManageStubInstance

object ProductManageListStubInstance {
    private var productManageListComponent: ProductManageListComponentStub? = null

    fun getComponent(context: Context): ProductManageListComponent {
        if (productManageListComponent == null) {
            val productManageComponent =
                ProductManageStubInstance.getComponent(context.applicationContext)
            productManageListComponent = DaggerProductManageListComponentStub
                .builder()
                .productManageComponentStub(productManageComponent)
                .productManageListModule(ProductManageListModule(context))
                .build()
        }

        return productManageListComponent!!
    }
}