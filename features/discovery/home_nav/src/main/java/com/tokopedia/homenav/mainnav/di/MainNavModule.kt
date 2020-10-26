package com.tokopedia.homenav.mainnav.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.NavCommandProcessor
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import dagger.Module
import dagger.Provides

@Module(includes = [
    MainNavUseCaseModule::class
])
class MainNavModule {

    @MainNavScope
    @Provides
    fun provideMainNavDispatcher(): NavDispatcherProvider {
        return MainNavDispatcherProviderImpl()
    }

    @MainNavScope
    @Provides
    fun provideMainNavCommandProcessor(dispatcher: NavDispatcherProvider) = NavCommandProcessor(dispatcher.io())

    @MainNavScope
    @Provides
    fun provideGetCategoryGroupUseCase(graphqlRepository: GraphqlRepository): GetCategoryGroupUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<DynamicHomeIconEntity>(graphqlRepository)
        return GetCategoryGroupUseCase(useCase)
    }
}