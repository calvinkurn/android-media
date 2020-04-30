package com.tokopedia.product.manage

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.manage.common.di.DaggerProductManageComponent
import com.tokopedia.product.manage.common.di.ProductManageComponent

class ProductManageInstance {
    companion object {
        private var productManageComponent: ProductManageComponent? = null

        fun getComponent(application: Application): ProductManageComponent {
            return productManageComponent?.run {
                productManageComponent
            } ?: DaggerProductManageComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }
}