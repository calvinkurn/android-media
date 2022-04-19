package com.tokopedia.digital.home.old.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

object DigitalHomePageComponentInstance {
    private lateinit var digitalHomepageComponent: DigitalHomePageComponent

    fun getDigitalHomepageComponent(application: Application): DigitalHomePageComponent {
        if (!::digitalHomepageComponent.isInitialized) {
            digitalHomepageComponent =  DaggerDigitalHomePageComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return digitalHomepageComponent
    }

}
