package com.tokopedia.universal_sharing.di

import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.linker.LinkerManager

open class ActivityComponentFactory {

    open fun createActivityComponent(): UniversalShareComponent {
        return DaggerUniversalShareComponent.builder()
            .baseAppComponent((LinkerManager.getInstance().context.applicationContext as BaseMainApplication).baseAppComponent)
            .universalShareModule(UniversalShareModule())
            .universalShareUseCaseModule(UniversalShareUseCaseModule())
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
