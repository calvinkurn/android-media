package com.tokopedia.brandlist

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.brandlist.common.di.BrandlistComponent
import com.tokopedia.brandlist.common.di.DaggerBrandlistComponent

class BrandlistInstance {
    companion object {
        private var brandlistComponent: BrandlistComponent? = null

        fun getComponent(application: Application): BrandlistComponent {
            return brandlistComponent?.run {
                brandlistComponent
            } ?: DaggerBrandlistComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
    }
}