package com.tokopedia.attachvoucher.di

import com.tokopedia.attachvoucher.data.GetVoucherResponse
import com.tokopedia.attachvoucher.data.voucherv2.GetMerchantPromotionGetMVListResponse
import com.tokopedia.attachvoucher.usecase.GetVoucherUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@Module
class AttachVoucherModule {

    @AttachVoucherScope
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @AttachVoucherScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository

//    @AttachVoucherScope
//    @Provides
//    fun provideGqlUseCase(graphqlRepository: GraphqlRepository): GraphqlUseCase<GetVoucherResponse> {
//        return GraphqlUseCase(graphqlRepository)
//    }

    @AttachVoucherScope
    @Provides
    fun provideUseCase(repository: GraphqlRepository): CoroutineUseCase<Map<String, Any>, GetMerchantPromotionGetMVListResponse> {
        return GetVoucherUseCase(repository, Dispatchers.IO)
    }
}