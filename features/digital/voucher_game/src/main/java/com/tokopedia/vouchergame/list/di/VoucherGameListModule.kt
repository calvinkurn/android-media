package com.tokopedia.vouchergame.list.di

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.vouchergame.list.usecase.VoucherGameListUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @author by resakemal on 13/08/19
 */
@Module
class VoucherGameListModule {

    @VoucherGameListScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @VoucherGameListScope
    @Provides
    fun provideGraphqlRepository(): GraphqlRepository {
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @VoucherGameListScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase =
            MultiRequestGraphqlUseCase(graphqlRepository)

    @VoucherGameListScope
    @Provides
    fun provideVoucherGameListUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase): VoucherGameListUseCase =
           VoucherGameListUseCase(multiRequestGraphqlUseCase)

}