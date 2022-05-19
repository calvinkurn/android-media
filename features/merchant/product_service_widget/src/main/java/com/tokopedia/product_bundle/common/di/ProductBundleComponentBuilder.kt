package com.tokopedia.product_bundle.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class ProductBundleComponentBuilder {

    companion object {
        private var productBundleComponent: ProductBundleComponent? = null

        fun getComponent(application: Application): ProductBundleComponent {
            return productBundleComponent?.run { productBundleComponent }
                    ?: DaggerProductBundleComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}