package com.tokopedia.rechargeocr.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

object RechargeCameraInstance {

    fun getComponent(application: Application): RechargeCameraComponent {
        val rechargeCameraComponent: RechargeCameraComponent by lazy {
            DaggerRechargeCameraComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .build()
        }
        return rechargeCameraComponent
    }
}