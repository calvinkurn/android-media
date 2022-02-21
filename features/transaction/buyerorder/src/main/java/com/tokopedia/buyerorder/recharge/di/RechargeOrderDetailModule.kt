package com.tokopedia.buyerorder.recharge.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides

/**
 * @author by furqan on 28/10/2021
 */
@Module
class RechargeOrderDetailModule {

    @RechargeOrderDetailScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository =
            GraphqlInteractor.getInstance().graphqlRepository

    @RechargeOrderDetailScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase =
            GraphqlInteractor.getInstance().multiRequestGraphqlUseCase

    @RechargeOrderDetailScope
    @Provides
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface =
            UserSession(context)

    @RechargeOrderDetailScope
    @Provides
    fun provideBestSellerMapper(@ApplicationContext context: Context) =
            BestSellerMapper(context)

}