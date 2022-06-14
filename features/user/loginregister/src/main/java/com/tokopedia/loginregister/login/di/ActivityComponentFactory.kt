package com.tokopedia.loginregister.login.di

import android.app.Activity
import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication

open class ActivityComponentFactory {

    open fun createActivityComponent(application: Application): LoginComponent {
        return DaggerLoginComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
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