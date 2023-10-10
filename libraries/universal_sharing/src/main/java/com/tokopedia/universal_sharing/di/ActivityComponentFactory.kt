package com.tokopedia.universal_sharing.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication

open class ActivityComponentFactory {

    open fun createActivityComponent(context: Context): UniversalShareComponent {
        return DaggerUniversalShareComponent.builder()
            .baseAppComponent((context as BaseMainApplication).baseAppComponent)
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
