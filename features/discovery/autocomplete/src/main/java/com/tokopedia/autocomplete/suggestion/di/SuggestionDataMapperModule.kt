package com.tokopedia.autocomplete.suggestion.di

import com.tokopedia.autocomplete.suggestion.SuggestionData
import com.tokopedia.autocomplete.suggestion.data.SuggestionDataMapper
import com.tokopedia.graphql.data.model.GraphqlResponse
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@SuggestionScope
@Module
class SuggestionDataMapperModule {

    @SuggestionScope
    @Provides
    fun provideSearchProductModelMapper(): Func1<GraphqlResponse, SuggestionData> {
        return SuggestionDataMapper()
    }
}