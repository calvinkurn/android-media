package com.tokopedia.vouchergame.list.di

import com.tokopedia.common_digital.common.di.DigitalCacheEnablerQualifier
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import dagger.Module
import dagger.Provides

/**
 * @author by resakemal on 13/08/19
 */
@Module
class VoucherGameListModule {

    @VoucherGameListScope
    @Provides
    fun provideVoucherGameListUseCase(
        graphqlRepository: GraphqlRepository,
        @DigitalCacheEnablerQualifier isEnableGqlCache: Boolean
    ): VoucherGameListUseCase =
           VoucherGameListUseCase(graphqlRepository, isEnableGqlCache)

}
