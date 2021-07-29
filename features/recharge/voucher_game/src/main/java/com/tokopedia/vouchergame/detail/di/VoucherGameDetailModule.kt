package com.tokopedia.vouchergame.detail.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import dagger.Module
import dagger.Provides


@Module
class VoucherGameDetailModule {

    @VoucherGameDetailScope
    @Provides
    fun provideVoucherGameListUseCase(graphqlRepository: GraphqlRepository): VoucherGameListUseCase =
           VoucherGameListUseCase(graphqlRepository)

}