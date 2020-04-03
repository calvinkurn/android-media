package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.*
import com.tokopedia.autocomplete.initialstate.data.InitialStateApi
import com.tokopedia.autocomplete.initialstate.data.InitialStateDataSource
import com.tokopedia.autocomplete.initialstate.data.InitialStateRepositoryImpl
import com.tokopedia.cachemanager.PersistentCacheManager
import dagger.Module
import dagger.Provides

@InitialStateScope
@Module
class InitialStateRepositoryModule {

    @InitialStateScope
    @Provides
    internal fun provideInitialStateRepository(
            @InitialStateQualifier initialStateApi: InitialStateApi,
            initialStateMapper: InitialStateMapper
    ): InitialStateRepository {
        return InitialStateRepositoryImpl(
                InitialStateDataSource(initialStateApi, initialStateMapper, PersistentCacheManager.instance)
        )
    }
}