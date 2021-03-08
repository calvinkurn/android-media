package com.tokopedia.ordermanagement.orderhistory.purchase.detail.di

import com.tokopedia.core.network.apiservices.transaction.OrderDetailService
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.OrderHistoryRepository
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.mapper.OrderDetailMapper
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.interactor.OrderHistoryInteractorImpl
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.presenter.OrderHistoryPresenterImpl
import dagger.Module
import dagger.Provides
import rx.subscriptions.CompositeSubscription

/**
 * Created by kris on 11/17/17. Tokopedia
 */
@Module
class OrderHistoryModule {
    @Provides
    @OrderHistoryScope
    fun provideCompositeSubscription(): CompositeSubscription {
        return CompositeSubscription()
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderDetailService(): OrderDetailService {
        return OrderDetailService()
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderDetailMapper(): OrderDetailMapper {
        return OrderDetailMapper()
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderHistoryRepository(): OrderHistoryRepository {
        return OrderHistoryRepository(
                provideOrderDetailService(),
                provideOrderDetailMapper())
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderHistoryInteractor(): OrderHistoryInteractorImpl {
        return OrderHistoryInteractorImpl(provideOrderHistoryRepository(),
                provideCompositeSubscription())
    }

    @Provides
    @OrderHistoryScope
    fun provideOrderHistoryPresenter(): OrderHistoryPresenterImpl {
        return OrderHistoryPresenterImpl(provideOrderHistoryInteractor())
    }
}