package com.tokopedia.product.manage

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.manage.common.di.DaggerProductManageComponent
import com.tokopedia.product.manage.common.di.ProductManageComponent

class ProductManageInstance {
    companion object {
        private var productManageComponent: ProductManageComponent? = null

        fun getComponent(context: Context): ProductManageComponent {
            return productManageComponent?.run {
                productManageComponent
            } ?: DaggerProductManageComponent.builder().baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
            ).build()
        }
    }
}