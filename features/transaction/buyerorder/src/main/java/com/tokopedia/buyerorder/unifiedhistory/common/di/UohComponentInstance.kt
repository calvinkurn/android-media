package com.tokopedia.buyerorder.unifiedhistory.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by fwidjaja on 04/07/20.
 */
object UohComponentInstance {
    private lateinit var uohComponent: UohComponent

    fun getUohComponent(application: Application): UohComponent {
        if (!::uohComponent.isInitialized) {
            uohComponent = DaggerUohComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return uohComponent
    }
}