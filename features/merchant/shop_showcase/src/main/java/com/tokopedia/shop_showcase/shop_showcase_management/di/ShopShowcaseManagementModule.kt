package com.tokopedia.shop_showcase.shop_showcase_management.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopShowcaseManagementViewModelModule::class])
@ShopShowcaseManagementScope
class ShopShowcaseManagementModule(val context: Context) {

    @Provides
    fun provideShopShowcaseManagementContext() = context

    @ShopShowcaseManagementScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopShowcaseManagementScope
    @Provides
    fun provideDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider

    @ShopShowcaseManagementScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ShopShowcaseManagementScope
    @Provides
    fun provideCreateShopShowcaseUseCase(
            gqlRepository: GraphqlRepository
    ): CreateShopShowcaseUseCase {
        return CreateShopShowcaseUseCase(gqlRepository)
    }

}