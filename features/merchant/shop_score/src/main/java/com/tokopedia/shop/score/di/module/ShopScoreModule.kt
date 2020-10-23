package com.tokopedia.shop.score.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.score.di.scope.ShopScoreScope
import com.tokopedia.shop.score.coroutine.CoroutineDispatchers
import com.tokopedia.shop.score.coroutine.CoroutineDispatchersProvider
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

@Module
@ShopScoreScope
class ShopScoreModule {

    @ShopScoreScope
    @Provides
    fun provideCoroutineDispatchers(): CoroutineDispatchers {
        return CoroutineDispatchersProvider
    }

    @ShopScoreScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @ShopScoreScope
    @Provides
    fun provideUserGrqphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }
}