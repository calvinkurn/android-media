package com.tokopedia.autocomplete.suggestion.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.suggestion.domain.usecase.SuggestionUseCase
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionResponse
import com.tokopedia.autocomplete.suggestion.domain.model.SuggestionUniverse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@SuggestionScope
@Module
class SuggestionUseCaseModule {

    @SuggestionScope
    @Provides
    internal fun provideSuggestionUseCase(
            @SuggestionContext context: Context,
            suggestionDataModelMapper: Func1<GraphqlResponse, SuggestionUniverse>
    ): UseCase<SuggestionUniverse> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_universe_suggestion),
                SuggestionResponse::class.java
        )
        return SuggestionUseCase(graphqlRequest, GraphqlUseCase(), suggestionDataModelMapper)
    }
}