package com.tokopedia.homenav.mainnav.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.homenav.common.dispatcher.NavDispatcherProvider
import com.tokopedia.homenav.common.util.ClientMenuGenerator
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactory
import com.tokopedia.homenav.mainnav.data.factory.MainNavDataFactoryImpl
import com.tokopedia.homenav.mainnav.data.mapper.AccountHeaderMapper
import com.tokopedia.homenav.mainnav.data.mapper.BuListMapper
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.homenav.mainnav.domain.usecases.GetCategoryGroupUseCase
import com.tokopedia.homenav.mainnav.domain.usecases.GetProfileDataUseCase
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
    fun provideMainNavDataFactory(@ApplicationContext context: Context,
                                  userSession: UserSessionInterface): MainNavDataFactory
            = MainNavDataFactoryImpl(context, userSession)

    @MainNavScope
    @Provides
    fun provideBuListMapper(mainNavDataFactory: MainNavDataFactory) = BuListMapper()

    @MainNavScope
    @Provides
    fun provideAccountHeaderMapper(accountHeaderMapper: AccountHeaderMapper,
                                   @ApplicationContext context: Context,
                                   userSession: UserSessionInterface
    ) = AccountHeaderMapper(
            context, userSession
    )

    @MainNavScope
    @Provides
    fun provideClientMenuGenerator(@ApplicationContext context: Context, userSession: UserSessionInterface) = ClientMenuGenerator(context, userSession)

}