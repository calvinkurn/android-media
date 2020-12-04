package com.tokopedia.homenav.mainnav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactory
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactoryImpl
import com.tokopedia.homenav.mainnav.data.mapper.MainNavMapper
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import com.tokopedia.user.session.UserSessionInterface
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
    fun provideGetCategoryGroupUseCase(graphqlRepository: GraphqlRepository): GetCategoryGroupUseCase {
        val useCase = com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase<DynamicHomeIconEntity>(graphqlRepository)
        return GetCategoryGroupUseCase(useCase)
    }

    @MainNavScope
    @Provides
    fun provideMainNavDataFactory(@ApplicationContext context: Context,
                                  userSession: UserSessionInterface): MainNavDataFactory
            = MainNavDataFactoryImpl(context, userSession)

    @MainNavScope
    @Provides
    fun provideMainNavMapper(mainNavDataFactory: MainNavDataFactory) = MainNavMapper(mainNavDataFactory)

    @MainNavScope
    @Provides
    fun provideClientMenuGenerator(@ApplicationContext context: Context, userSession: UserSessionInterface) = ClientMenuGenerator(context, userSession)

}