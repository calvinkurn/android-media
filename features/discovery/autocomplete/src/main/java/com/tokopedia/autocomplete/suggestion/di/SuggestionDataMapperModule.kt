package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.data.SuggestionDataMapper
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.graphql.data.model.GraphqlResponse
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@SuggestionScope
@Module
class SuggestionDataMapperModule {

    @SuggestionScope
    @Provides
    fun provideSearchProductModelMapper(): Func1<GraphqlResponse, SuggestionUniverse> {
        return SuggestionDataMapper()
    }
}