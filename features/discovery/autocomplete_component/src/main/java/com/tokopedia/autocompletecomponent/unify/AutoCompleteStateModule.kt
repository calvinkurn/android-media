package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.di.AutoCompleteScope
import com.tokopedia.discovery.common.model.SearchParameter
import dagger.Module
import dagger.Provides

@Module
class AutoCompleteStateModule(
    private val searchParameter: SearchParameter
) {

    @Provides
    @AutoCompleteScope
    fun provideAutoCompleteState() = AutoCompleteState(searchParameter)
}
