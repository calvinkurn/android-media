package com.tokopedia.universal_sharing.di

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication

/**
 * Stub-able class for Instrument Test
 */
open class UniversalSharingComponentFactory {

    open fun createComponent(application: Application): UniversalShareComponent {
        return DaggerUniversalShareComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        private var sInstance: UniversalSharingComponentFactory? = null

        @VisibleForTesting
        var instance: UniversalSharingComponentFactory
            get() {
                if (sInstance == null) sInstance = UniversalSharingComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
