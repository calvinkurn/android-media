package com.tokopedia.sellerorder

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.sellerorder.common.di.DaggerSomComponent
import com.tokopedia.sellerorder.common.di.SomComponent

/**
 * Created by fwidjaja on 2019-08-28.
 */
object SomComponentInstance {
    private lateinit var somComponent: SomComponent

    fun getSomComponent(application: Application): SomComponent {
        if (!::somComponent.isInitialized) {
            somComponent = DaggerSomComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return somComponent
    }
}