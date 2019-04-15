package com.tokopedia.product.detail.estimasiongkir.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.RawQueryKeyConstant
import com.tokopedia.user.session.UserSessionInterface

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import javax.inject.Named

@Module
@RatesEstimationScope
class RatesEstimationModule {

    @RatesEstimationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @RatesEstimationScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @Provides
    fun provideGraphQlRepository() = GraphqlInteractor.getInstance().graphqlRepository

    @RatesEstimationScope
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @RatesEstimationScope
    @Provides
    @Named(RawQueryKeyConstant.QUERY_GET_RATE_ESTIMATION)
    fun provideRawGetRateEstimation(@ApplicationContext context: Context): String =
            GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_rates_estimation_v3)
}
