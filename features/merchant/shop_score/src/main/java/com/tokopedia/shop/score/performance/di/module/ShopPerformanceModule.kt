package com.tokopedia.shop.score.performance.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module(includes = [ShopPerformanceViewModelModule::class])
class ShopPerformanceModule {

    @ShopPerformanceScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @ShopPerformanceScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopPerformanceScope
    @Provides
    fun provideUserGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}