package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier
import com.tokopedia.autocomplete.initialstate.*
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchResponseMapper
import com.tokopedia.cachemanager.PersistentCacheManager
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class InitialStateRepositoryModule {

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateRepository(
            @AutoCompleteQualifier initialStateApi: InitialStateApi,
            initialStateMapper: InitialStateMapper,
            popularSearchResponseMapper: PopularSearchResponseMapper
    ): InitialStateRepository {
        return InitialStateRepositoryImpl(
                InitialStateDataSource(initialStateApi, initialStateMapper, popularSearchResponseMapper, PersistentCacheManager.instance)
        )
    }
}