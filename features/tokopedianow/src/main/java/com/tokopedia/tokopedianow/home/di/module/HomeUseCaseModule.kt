package com.tokopedia.tokopedianow.home.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.di.scope.HomeScope
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase
import dagger.Module
import dagger.Provides

@Module
class HomeUseCaseModule {

    @HomeScope
    @Provides
    fun provideGetHomeLayoutUseCase(graphqlRepository: GraphqlRepository): GetHomeLayoutListUseCase {
        return GetHomeLayoutListUseCase(graphqlRepository)
    }
}