package com.tokopedia.shop.showcase.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.showcase.di.scope.ShopPageShowcaseScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * Created by Rafli Syam on 05/03/2021
 */
@Module(includes = [ShopPageShowcaseViewModelModule::class])
class ShopPageShowcaseModule {

    @ShopPageShowcaseScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPageShowcaseScope
    @Provides
    fun getCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @ShopPageShowcaseScope
    @Provides
    fun provideGetShopEtalaseByShopUsecase(@ApplicationContext context: Context): GetShopEtalaseByShopUseCase {
        return GetShopEtalaseByShopUseCase(context)
    }

}