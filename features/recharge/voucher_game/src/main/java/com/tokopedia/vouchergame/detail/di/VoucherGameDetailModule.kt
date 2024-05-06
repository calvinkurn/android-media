package com.tokopedia.vouchergame.detail.di

import com.tokopedia.common.topupbills.analytics.CommonMultiCheckoutAnalytics
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import dagger.Module
import dagger.Provides


@Module
class VoucherGameDetailModule {
    @Provides
    fun provideGraphqlUseCaseCatalogData(graphqlRepository: GraphqlRepository): GraphqlUseCase<CatalogData.Response> {
        return GraphqlUseCase(graphqlRepository)
    }

    @VoucherGameDetailScope
    @Provides
    fun provideVoucherGameListUseCase(
        graphqlRepository: GraphqlRepository,
        remoteConfig: RemoteConfig
    ): VoucherGameListUseCase =
           VoucherGameListUseCase(graphqlRepository, remoteConfig)

    @VoucherGameDetailScope
    @Provides
    fun provideCatalogProductInputUseCase(
        graphqlUseCase: GraphqlUseCase<CatalogData.Response>,
        remoteConfig: RemoteConfig
    ): RechargeCatalogProductInputUseCase =
           RechargeCatalogProductInputUseCase(graphqlUseCase, remoteConfig)

    @VoucherGameDetailScope
    @Provides
    fun provideAnalyticsCommonMultiCheckout(): CommonMultiCheckoutAnalytics {
        return CommonMultiCheckoutAnalytics()
    }


}
