package com.tokopedia.autocomplete.suggestion.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.suggestion.SuggestionGqlUseCase
import com.tokopedia.autocomplete.suggestion.data.SuggestionResponse
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
            @ApplicationContext context: Context,
            suggestionDataModelMapper: Func1<GraphqlResponse, SuggestionResponse>
    ): UseCase<SuggestionResponse> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_universe_suggestion),
                SuggestionResponse::class.java
        )
        return SuggestionGqlUseCase(graphqlRequest, GraphqlUseCase(), suggestionDataModelMapper)
    }
}