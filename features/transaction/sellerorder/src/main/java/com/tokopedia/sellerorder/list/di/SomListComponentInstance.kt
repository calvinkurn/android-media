package com.tokopedia.sellerorder.list.di

import android.app.Application
import com.tokopedia.sellerorder.SomComponentInstance

object SomListComponentInstance {
    private var somListComponent: SomListComponent? = null

    fun getSomComponent(application: Application): SomListComponent {
        return somListComponent.run { somListComponent }
                ?: DaggerSomListComponent.builder().somComponent(SomComponentInstance.getSomComponent(application))
                        .build()
    }
}