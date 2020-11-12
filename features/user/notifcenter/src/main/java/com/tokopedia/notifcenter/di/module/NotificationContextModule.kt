package com.tokopedia.notifcenter.di.module

import android.content.Context
import com.tokopedia.notifcenter.di.scope.NotificationScope
import dagger.Module
import dagger.Provides

@Module
class NotificationContextModule(private val context: Context) {

    @Provides
    @NotificationScope
    fun provideContext(): Context {
        return context
    }

}