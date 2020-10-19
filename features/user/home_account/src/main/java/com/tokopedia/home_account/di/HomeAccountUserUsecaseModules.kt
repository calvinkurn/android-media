package com.tokopedia.home_account.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import dagger.Module
import dagger.Provides


@Module
class HomeAccountUserUsecaseModules {

    @HomeAccountUserScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @HomeAccountUserScope
    @Provides
    fun provideMultiRequestGraphql(): MultiRequestGraphqlUseCase = GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

//    @HomeAccountUserScope
//    @Provides
//    fun provideAdditionalCheckUseCase(graphqlRepository: GraphqlRepository)
//            : GraphqlUseCase<GetObjectPojo> = GraphqlUseCase(graphqlRepository)
}