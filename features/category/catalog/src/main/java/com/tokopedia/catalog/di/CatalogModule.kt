package com.tokopedia.catalog.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Module
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl


@Module
class CatalogModule {
    @CatalogScope
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }
}
