package com.tokopedia.autocomplete.initialstate.di

import com.tokopedia.autocomplete.di.AutoCompleteScope
import com.tokopedia.autocomplete.initialstate.InitialStateMapper
import dagger.Module
import dagger.Provides

@AutoCompleteScope
@Module
class InitialStateMapperModule {

    @AutoCompleteScope
    @Provides
    internal fun provideInitialStateMapper(): InitialStateMapper {
        return InitialStateMapper()
    }
}