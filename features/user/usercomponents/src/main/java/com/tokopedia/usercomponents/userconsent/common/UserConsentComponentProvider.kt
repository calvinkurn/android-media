package com.tokopedia.usercomponents.userconsent.common

import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.usercomponents.userconsent.di.DaggerUserConsentComponent
import com.tokopedia.usercomponents.userconsent.di.UserConsentComponent

object UserConsentComponentProvider {

    private var userConsentComponent: UserConsentComponent? = null

    fun setUserConsentComponent(component: UserConsentComponent) {
        this.userConsentComponent = component
    }

    fun getUserConsentComponent(context: Context): UserConsentComponent? {
        return if (userConsentComponent == null) {
            DaggerUserConsentComponent.builder()
                .baseAppComponent(
                    (context.applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
        } else {
            userConsentComponent
        }
    }
}
