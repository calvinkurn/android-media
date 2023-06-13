package com.tokopedia.user.session.di

import android.content.Context
import androidx.annotation.VisibleForTesting

open class ComponentFactory {

    open fun createUserSessionComponent(context: Context): UserSessionComponent {
        return DaggerUserSessionComponent.builder()
            .context(context)
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