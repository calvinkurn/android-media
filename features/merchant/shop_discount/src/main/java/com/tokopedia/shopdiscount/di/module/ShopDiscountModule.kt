package com.tokopedia.shopdiscount.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.shopdiscount.di.scope.ShopDiscountComponentScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class ShopDiscountModule {

    @ShopDiscountComponentScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @ShopDiscountComponentScope
    @Provides
    fun provideGraphqlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @ShopDiscountComponentScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)
}