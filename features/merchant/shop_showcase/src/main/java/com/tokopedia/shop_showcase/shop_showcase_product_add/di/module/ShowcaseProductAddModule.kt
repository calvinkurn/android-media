package com.tokopedia.shop_showcase.shop_showcase_product_add.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.Interactor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop_showcase.common.ShopShowcaseTracking
import com.tokopedia.shop_showcase.shop_showcase_product_add.di.scope.ShowcaseProductAddScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named

/**
 * @author by Rafli Syam on 2020-03-09
 */

@ShowcaseProductAddScope
@Module(includes = [ShowcaseProductAddViewModelModule::class])
class ShowcaseProductAddModule {

    @ShowcaseProductAddScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ShowcaseProductAddScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = Interactor.getInstance().graphqlRepository

    @ShowcaseProductAddScope
    @Provides
    @Named("Main")
    fun provideMainDispatcherProvider(): CoroutineDispatcher = Dispatchers.Main

    @ShowcaseProductAddScope
    @Provides
    fun provideShopShowcaseTracking(@ApplicationContext context: Context?): ShopShowcaseTracking {
        return ShopShowcaseTracking(context)
    }
}