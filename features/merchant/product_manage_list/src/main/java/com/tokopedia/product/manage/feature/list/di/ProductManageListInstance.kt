package com.tokopedia.product.manage.feature.list.di

import android.content.Context
import com.tokopedia.product.manage.common.ProductManageInstance

object ProductManageListInstance {
    private var productManageListComponent: ProductManageListComponent? = null

    fun getComponent(context: Context): ProductManageListComponent {
        if (productManageListComponent == null) {
            val productManageComponent = ProductManageInstance.getComponent(context.applicationContext)
            productManageListComponent = DaggerProductManageListComponent
                    .builder()
                    .productManageComponent(productManageComponent)
                    .productManageListModule(ProductManageListModule(context))
                    .build()
        }

        return productManageListComponent!!
    }
}