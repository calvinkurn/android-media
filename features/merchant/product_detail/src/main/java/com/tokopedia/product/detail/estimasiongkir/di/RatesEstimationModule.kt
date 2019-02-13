package com.tokopedia.product.detail.estimasiongkir.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.detail.estimasiongkir.domain.interactor.GetRateEstimationUseCase
import com.tokopedia.user.session.UserSessionInterface

import dagger.Module
import dagger.Provides

@Module
@RatesEstimationScope
class RatesEstimationModule {

    @RatesEstimationScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context): UserSessionInterface = com.tokopedia.user.session.UserSession(context)

    @RatesEstimationScope
    @Provides
    fun provideGraphqlUseCase() = GraphqlUseCase()

    @RatesEstimationScope
    @Provides
    fun provideGetRateEstimationUseCase(graphqlUseCase: GraphqlUseCase): GetRateEstimationUseCase {
        return GetRateEstimationUseCase(graphqlUseCase)
    }
}
