package com.tokopedia.inbox.universalinbox.di

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.tokochat.config.util.TokoChatConnection

open class UniversalInboxActivityComponentFactory {

    open fun createUniversalInboxComponent(application: Application): UniversalInboxComponent {
        return DaggerUniversalInboxComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokoChatConfigComponent(TokoChatConnection.getComponent(application.applicationContext))
            .build()
    }

    companion object {
        private var sInstance: UniversalInboxActivityComponentFactory? = null

        @VisibleForTesting
        var instance: UniversalInboxActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = UniversalInboxActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
