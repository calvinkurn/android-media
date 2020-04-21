package com.tokopedia.product.manage.feature.list.di

import android.app.Application
import com.tokopedia.product.manage.ProductManageInstance

object ProductManageListInstance {
    private var productManageListComponent: ProductManageListComponent? = null

    fun getComponent(application: Application): ProductManageListComponent {
        if(productManageListComponent == null) {
            val productManageComponent = ProductManageInstance.getComponent(application)
            productManageListComponent = DaggerProductManageListComponent
                .builder()
                .productManageComponent(productManageComponent)
                .build()
        }

        return productManageListComponent!!
    }
}