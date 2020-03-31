package com.tokopedia.shop_showcase.shop_showcase_add.di.modules

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop_showcase.shop_showcase_add.di.scope.ShopShowcaseAddScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

@ShopShowcaseAddScope
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
    @Named("Main")
    fun provideMainDispatcherProvider(): CoroutineDispatcher = Dispatchers.Main
}