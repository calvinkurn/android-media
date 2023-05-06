package com.tokopedia.home_account.di

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication

open class ActivityComponentFactory {

    open fun createHomeAccountComponent(
        context: Context,
        application: Application
    ): HomeAccountUserComponents {
        return DaggerHomeAccountUserComponents.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .homeAccountUserModules(HomeAccountUserModules(context))
            .build()
    }

    companion object {
        private var sInstance: ActivityComponentFactory? = null

        @VisibleForTesting
        var instance: ActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = ActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
