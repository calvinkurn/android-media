package com.tokopedia.analyticsdebugger.cassava.di

import android.app.Activity
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * @author by furqan on 07/04/2021
 */
object CassavaComponentInstance {

    private var cassavaComponent: CassavaComponent? = null

    fun getInstance(activity: Activity): CassavaComponent {
        cassavaComponent?.let {
            return it
        }

        cassavaComponent = DaggerCassavaComponent.builder()
                .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
                .cassavaModule(CassavaModule(activity))
                .build()

        return cassavaComponent!!
    }
}