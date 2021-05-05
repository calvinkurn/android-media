package com.tokopedia.tokomart.home.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomart.home.di.scope.TokoMartHomeScope
import com.tokopedia.tokomart.home.domain.usecase.GetHomeLayoutUseCase
import dagger.Module
import dagger.Provides

@Module
class TokoMartHomeUseCaseModule {

    @TokoMartHomeScope
    @Provides
    fun provideGetHomeLayoutUseCase(graphqlRepository: GraphqlRepository): GetHomeLayoutUseCase {
        return GetHomeLayoutUseCase(graphqlRepository)
    }
}