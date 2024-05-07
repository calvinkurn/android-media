package com.tokopedia.autocompletecomponent.unify.byteio

import com.tokopedia.autocompletecomponent.di.AutoCompleteScope
import dagger.Binds
import dagger.Module

@Module
abstract class SugSessionIdModule {

    @Binds
    @AutoCompleteScope
    abstract fun providesSugSessionId(sugSessionIdImpl: SugSessionIdImpl): SugSessionId
}
