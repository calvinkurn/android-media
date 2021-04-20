package com.tokopedia.shop_showcase.shop_showcase_add.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop_showcase.common.ShopShowcaseTracking
import com.tokopedia.shop_showcase.shop_showcase_add.di.scope.ShopShowcaseAddScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopShowcaseAddUseCaseModule::class, ShopShowcaseAddViewModelModule::class])
class ShopShowcaseAddModule {
    @ShopShowcaseAddScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShopShowcaseAddScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ShopShowcaseAddScope
    @Provides
    fun provideShopShowcaseTracking(@ApplicationContext context: Context?): ShopShowcaseTracking {
        return ShopShowcaseTracking(context)
    }

    @ShopShowcaseAddScope
    @Provides
    fun provideDispatcherProvider(): CoroutineDispatchers = CoroutineDispatchersProvider
}