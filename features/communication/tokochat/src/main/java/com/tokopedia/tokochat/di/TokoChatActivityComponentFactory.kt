package com.tokopedia.tokochat.di

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.tokochat.config.util.TokoChatConnection
import com.tokopedia.abstraction.base.app.BaseMainApplication

open class TokoChatActivityComponentFactory {

    open fun createTokoChatComponent(application: Application): TokoChatComponent {
        return DaggerTokoChatComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .tokoChatConfigComponent(TokoChatConnection.getComponent(application.applicationContext))
            .build()
    }

    companion object {
        private var sInstance: TokoChatActivityComponentFactory? = null

        @VisibleForTesting
        var instance: TokoChatActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = TokoChatActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
