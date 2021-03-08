package com.tokopedia.ordermanagement.orderhistory.purchase.detail.interactor

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.OrderHistoryRepository
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * Created by kris on 11/17/17. Tokopedia
 */
class OrderHistoryInteractorImpl(private val repository: OrderHistoryRepository,
                                 private val compositeSubscription: CompositeSubscription) : OrderHistoryInteractor {
    override fun requestOrderHistoryData(subscriber: Subscriber<OrderHistoryData>,
                                         params: HashMap<String?, Any?>?) {
        compositeSubscription.add(repository.requestOrderHistoryData(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber))
    }

    override fun onViewDestroyed() {
        compositeSubscription.unsubscribe()
    }

}