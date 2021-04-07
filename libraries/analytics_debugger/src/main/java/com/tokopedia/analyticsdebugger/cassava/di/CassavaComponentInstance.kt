package com.tokopedia.analyticsdebugger.cassava.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * @author by furqan on 07/04/2021
 */
object CassavaComponentInstance {

    private var cassavaComponent: CassavaComponent? = null

    fun getInstance(application: Application): CassavaComponent {
        cassavaComponent?.let {
            return it
        }

        cassavaComponent = DaggerCassavaComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()

        return cassavaComponent!!
    }
}