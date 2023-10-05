package com.tokopedia.notifcenter.di

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.tokopedia.abstraction.base.app.BaseMainApplication


open class NotificationActivityComponentFactory {

    open fun createNotificationComponent(application: Application): NotificationComponent {
        return DaggerNotificationComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    companion object {
        private var sInstance: NotificationActivityComponentFactory? = null

        @VisibleForTesting
        var instance: NotificationActivityComponentFactory
            get() {
                if (sInstance == null) sInstance = NotificationActivityComponentFactory()
                return sInstance!!
            }
            set(instance) {
                sInstance = instance
            }
    }
}
