package com.tokopedia.buyerorder.unifiedhistory.list.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Created by fwidjaja on 13/11/20.
 */
object UohListComponentInstance {
    private lateinit var uohListComponent: UohListComponent

    fun getUohListComponent(application: Application): UohListComponent {
        if (!::uohListComponent.isInitialized) {
            uohListComponent = DaggerUohListComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return uohListComponent
    }
}