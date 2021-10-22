package com.tokopedia.autocompletecomponent.initialstate.data

import com.tokopedia.autocompletecomponent.initialstate.di.InitialStateScope
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