package com.tokopedia.tokopedianow.home.di.factory

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokopedianow.home.di.component.DaggerHomeComponent
import com.tokopedia.tokopedianow.home.di.component.HomeComponent

open class HomeComponentFactory {

    open fun createComponent(application: Application): HomeComponent {
        return DaggerHomeComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        private var sInstance: HomeComponentFactory? = null

        @VisibleForTesting
        var instance: HomeComponentFactory
            get() {
                if (sInstance == null) sInstance = HomeComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
