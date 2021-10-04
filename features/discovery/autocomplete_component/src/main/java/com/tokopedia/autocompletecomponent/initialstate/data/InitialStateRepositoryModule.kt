package com.tokopedia.autocompletecomponent.initialstate.data

import com.tokopedia.autocompletecomponent.initialstate.domain.refreshinitialstate.InitialStateRepository
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateQualifier
import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
import dagger.Module
import dagger.Provides

@Module(
    includes = [
        InitialStateNetModule::class,
        InitialStateMapperModule::class,
    ]
)
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