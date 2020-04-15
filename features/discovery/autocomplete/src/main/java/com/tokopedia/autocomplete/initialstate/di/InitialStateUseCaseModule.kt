package com.tokopedia.autocomplete.initialstate.di

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.data.InitialStateGqlResponse
import com.tokopedia.autocomplete.initialstate.InitialStateUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.UseCase
import dagger.Module
import dagger.Provides
import rx.functions.Func1

@InitialStateScope
@Module
class InitialStateUseCaseModule {

    @InitialStateScope
    @Provides
    internal fun provideInitialStateUseCase(
            @InitialStateContext context: Context,
            initialStateDataModelMapper: Func1<GraphqlResponse, List<InitialStateData>>
    ): UseCase<List<InitialStateData>> {
        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, R.raw.gql_universe_initial_state),
                InitialStateGqlResponse::class.java
        )
        return InitialStateUseCase(graphqlRequest, GraphqlUseCase(), initialStateDataModelMapper)
    }
}