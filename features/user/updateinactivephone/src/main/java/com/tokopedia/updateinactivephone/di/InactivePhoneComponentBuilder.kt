package com.tokopedia.updateinactivephone.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.updateinactivephone.di.module.InactivePhoneModule

object InactivePhoneComponentBuilder {

    fun getComponent(application: Context): InactivePhoneComponent {
        return DaggerInactivePhoneComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .inactivePhoneModule(InactivePhoneModule())
            .build()
    }
}