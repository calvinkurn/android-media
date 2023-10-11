package com.tokopedia.stories.widget.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import dagger.Module
import dagger.Provides

/**
 * Created by kenny.hadisaputra on 08/08/23
 */
@Module
object StoriesRemoteConfigModule {

    @Provides
    fun provideRemoteConfigInstance(@ApplicationContext context: Context): RemoteConfig {
        return FirebaseRemoteConfigImpl(context)
    }
}
