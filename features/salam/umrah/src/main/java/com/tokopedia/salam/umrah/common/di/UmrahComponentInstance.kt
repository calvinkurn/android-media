package com.tokopedia.salam.umrah.common.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * @author by furqan on 08/10/2019
 */
object UmrahComponentInstance {

    private lateinit var umrahComponent: UmrahComponent

    fun getUmrahComponent(application: Application): UmrahComponent {
        if (!::umrahComponent.isInitialized) {
            umrahComponent = DaggerUmrahComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }

        return umrahComponent
    }

}