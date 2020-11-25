package com.tokopedia.additional_check.di

import com.tokopedia.additional_check.data.GetObjectPojo
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides


@Module
class AdditionalCheckUseCaseModules {

    @AdditionalCheckScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @AdditionalCheckScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @AdditionalCheckScope
    @Provides
    fun provideAdditionalCheckUseCase(graphqlRepository: GraphqlRepository)
            : GraphqlUseCase<GetObjectPojo> = GraphqlUseCase(graphqlRepository)
}