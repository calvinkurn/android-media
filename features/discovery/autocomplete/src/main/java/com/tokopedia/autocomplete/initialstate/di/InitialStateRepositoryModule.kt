package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateMapper
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.autocomplete.initialstate.data.InitialStateApi
import com.tokopedia.autocomplete.initialstate.data.InitialStateDataSource
import com.tokopedia.autocomplete.initialstate.data.InitialStateRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class InitialStateRepositoryModule {

    @InitialStateScope
    @Provides
    internal fun provideInitialStateRepository(
            @InitialStateQualifier initialStateApi: InitialStateApi,
            initialStateMapper: InitialStateMapper
    ): InitialStateRepository {
        return InitialStateRepositoryImpl(
                InitialStateDataSource(initialStateApi, initialStateMapper)
        )
    }
}