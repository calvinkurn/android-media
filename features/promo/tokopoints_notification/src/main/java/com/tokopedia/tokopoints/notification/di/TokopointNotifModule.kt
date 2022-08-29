package com.tokopedia.tokopoints.notification.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlResponse
import dagger.Module
import dagger.Provides

@Module
class TokopointNotifModule {
    @Provides
    fun getMultiGraphQlRequest(graphqlRepository: GraphqlRepository) = MultiRequestGraphqlUseCase(graphqlRepository)

    @Provides
    fun getGQLUserCase(graphqlRepository: GraphqlRepository) = GraphqlUseCase<GraphqlResponse>(graphqlRepository)

}