package com.tokopedia.user.session.di

import android.app.Application
import androidx.annotation.VisibleForTesting

open class ComponentFactory {

    open fun createUserSessionComponent(application: Application): UserSessionComponent {
        return DaggerUserSessionComponent.builder()
            .context(application)
            .build()
    }

    companion object {
        private var sInstance: ComponentFactory? = null

        @VisibleForTesting
        var instance: ComponentFactory
            get() {
                if (sInstance == null) sInstance = ComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}