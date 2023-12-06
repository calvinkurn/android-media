package com.tokopedia.content.product.preview.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object ProductPreviewInjector {

    private var customComponent: ProductPreviewComponent? = null

    fun get(context: Context): ProductPreviewComponent = synchronized(this) {
        return customComponent ?: DaggerProductPreviewComponent.builder()
            .baseAppComponent(
                (context.applicationContext as BaseMainApplication).baseAppComponent
            )
            .build()
    }

    fun set(component: ProductPreviewComponent) = synchronized(this) {
        customComponent = component
    }
}
