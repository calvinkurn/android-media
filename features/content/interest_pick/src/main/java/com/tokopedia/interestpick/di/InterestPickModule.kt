package com.tokopedia.interestpick.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.interestpick.data.pojo.GetInterestData
import com.tokopedia.interestpick.data.pojo.UpdateInterestData
import dagger.Module
import dagger.Provides

@Module
class InterestPickModule {

    @Provides
    fun provideGraphQlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    fun provideGetInterestUseCae(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetInterestData> = GraphqlUseCase(graphqlRepository)

    @Provides
    fun provideUpdateInterestUseCae(graphqlRepository: GraphqlRepository): GraphqlUseCase<UpdateInterestData> = GraphqlUseCase(graphqlRepository)

}