package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.initialstate.InitialStateMapper
import dagger.Module
import dagger.Provides

@Module
class InitialStateMapperModule {

    @InitialStateScope
    @Provides
    internal fun provideInitialStateMapper(): InitialStateMapper {
        return InitialStateMapper()
    }
}