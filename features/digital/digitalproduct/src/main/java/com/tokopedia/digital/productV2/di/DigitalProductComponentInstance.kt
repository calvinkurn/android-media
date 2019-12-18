package com.tokopedia.digital.productV2.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.digital.productV2.di.DaggerDigitalProductComponent

object DigitalProductComponentInstance {
    private lateinit var digitalProductComponent: DigitalProductComponent

    fun getDigitalProductComponent(application: Application): DigitalProductComponent {
        if (!::digitalProductComponent.isInitialized) {
            digitalProductComponent =  DaggerDigitalProductComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return digitalProductComponent
    }

}
