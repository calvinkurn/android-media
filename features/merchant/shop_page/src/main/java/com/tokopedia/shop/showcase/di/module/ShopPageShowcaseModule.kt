package com.tokopedia.shop.showcase.di.module

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shop.analytic.ShopPageShowcaseTracking
import com.tokopedia.shop.showcase.di.scope.ShopPageShowcaseScope
import com.tokopedia.trackingoptimizer.TrackingQueue
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
    fun provideGqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @ShopPageShowcaseScope
    @Provides
    fun provideTrackingQueue(
            @ApplicationContext context: Context
    ): TrackingQueue {
        return TrackingQueue(context)
    }

    @ShopPageShowcaseScope
    @Provides
    fun provideShopPageShowcaseTracking(
            trackingQueue: TrackingQueue
    ): ShopPageShowcaseTracking {
        return ShopPageShowcaseTracking(trackingQueue)
    }

}