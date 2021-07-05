package com.tokopedia.product.manage.common

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.manage.common.di.DaggerProductManageCommonComponent
import com.tokopedia.product.manage.common.di.ProductManageCommonComponent

class ProductManageCommonInstance {

    companion object {
        private var productManageCommonComponent: ProductManageCommonComponent? = null

        fun getComponent(context: Context): ProductManageCommonComponent {
            return productManageCommonComponent?.run {
                productManageCommonComponent
            } ?: DaggerProductManageCommonComponent.builder().baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
            ).build()
        }
    }
}