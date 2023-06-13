package com.tokopedia.product_ar.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ProductArModule(val productId: String, val shopId: String) {

    @ProductArScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

    @ProductArScope
    @Provides
    fun provideTrackingQueue(@ApplicationContext context: Context) = TrackingQueue(context)

    @Provides
    @ProductArScope
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = UserSession(context)

    @Provides
    @ProductArScope
    @Named(PRODUCT_ID_PROVIDED)
    fun provideProductId(): String = productId

    @Provides
    @ProductArScope
    @Named(SHOP_ID_PROVIDED)
    fun provideShopId(): String = shopId
}