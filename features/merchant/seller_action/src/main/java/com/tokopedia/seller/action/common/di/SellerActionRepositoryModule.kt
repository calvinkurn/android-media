package com.tokopedia.seller.action.common.di

import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.order.domain.repository.SliceMainOrderListRepository
import com.tokopedia.seller.action.order.domain.repository.SliceMainOrderListRepositoryImpl
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import dagger.Module
import dagger.Provides

@SellerActionScope
@Module
class SellerActionRepositoryModule {

    @SellerActionScope
    @Provides
    fun provideMainOrderListRepository(
            sliceMainOrderListUseCase: SliceMainOrderListUseCase,
            dispatcher: SellerActionDispatcherProvider): SliceMainOrderListRepository {
        return SliceMainOrderListRepositoryImpl(sliceMainOrderListUseCase, dispatcher)
    }

}