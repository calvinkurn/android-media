package com.tokopedia.appdownloadmanager_common.di.module

import android.app.DownloadManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.appdownloadmanager_common.di.scope.DownloadManagerScope
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

@Module
class DownloadManagerModule {

    @Provides
    @DownloadManagerScope
    fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager? {
        return ContextCompat.getSystemService(context, DownloadManager::class.java)
    }
}
