package com.tokopedia.usercomponents.userconsent.di

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication

object UserConsentComponentBuilder {

    fun getComponent(context: Context): UserConsentComponent {
        return DaggerUserConsentComponent.builder()
            .baseAppComponent((context.applicationContext as BaseMainApplication).baseAppComponent)
            .userConsentModule(UserConsentModule())
            .build()
    }
}