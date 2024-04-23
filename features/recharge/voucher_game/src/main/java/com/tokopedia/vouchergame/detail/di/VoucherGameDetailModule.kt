package com.tokopedia.vouchergame.detail.di

import com.tokopedia.common.topupbills.analytics.CommonMultiCheckoutAnalytics
import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
import com.tokopedia.common_digital.common.di.DigitalCacheEnablerQualifier
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
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
        @DigitalCacheEnablerQualifier isEnableGqlCache: Boolean
    ): VoucherGameListUseCase =
           VoucherGameListUseCase(graphqlRepository, isEnableGqlCache)

    @VoucherGameDetailScope
    @Provides
    fun provideCatalogProductInputUseCase(
        graphqlUseCase: GraphqlUseCase<CatalogData.Response>,
        @DigitalCacheEnablerQualifier isEnableGqlCache: Boolean
    ): RechargeCatalogProductInputUseCase =
           RechargeCatalogProductInputUseCase(graphqlUseCase, isEnableGqlCache)

    @VoucherGameDetailScope
    @Provides
    fun provideAnalyticsCommonMultiCheckout(): CommonMultiCheckoutAnalytics {
        return CommonMultiCheckoutAnalytics()
    }


}
