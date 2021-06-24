package com.tokopedia.tokopedianow.home.di.module

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.di.scope.TokoMartHomeScope
import com.tokopedia.tokopedianow.home.domain.usecase.GetHomeLayoutListUseCase
import dagger.Module
import dagger.Provides

@Module
class TokoMartHomeUseCaseModule {

    @TokoMartHomeScope
    @Provides
    fun provideGetHomeLayoutUseCase(graphqlRepository: GraphqlRepository): GetHomeLayoutListUseCase {
        return GetHomeLayoutListUseCase(graphqlRepository)
    }
}