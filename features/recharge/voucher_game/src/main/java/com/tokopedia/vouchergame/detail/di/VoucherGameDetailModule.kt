package com.tokopedia.vouchergame.detail.di

import com.tokopedia.common.topupbills.data.product.CatalogData
import com.tokopedia.common.topupbills.usecase.RechargeCatalogProductInputUseCase
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
    fun provideVoucherGameListUseCase(graphqlRepository: GraphqlRepository): VoucherGameListUseCase =
           VoucherGameListUseCase(graphqlRepository)

    @VoucherGameDetailScope
    @Provides
    fun provideCatalogProductInputUseCase(graphqlUseCase: GraphqlUseCase<CatalogData.Response>): RechargeCatalogProductInputUseCase =
           RechargeCatalogProductInputUseCase(graphqlUseCase)

}