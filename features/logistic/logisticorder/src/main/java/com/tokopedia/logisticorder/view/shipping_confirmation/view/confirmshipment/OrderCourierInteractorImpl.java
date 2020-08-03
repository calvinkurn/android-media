package com.tokopedia.logisticorder.view.shipping_confirmation.view.confirmshipment;


import com.tokopedia.logisticdata.data.repository.OrderCourierRepository;
import com.tokopedia.logisticorder.view.shipping_confirmation.network.mapper.OrderDetailMapper;
import com.tokopedia.logisticorder.view.shipping_confirmation.view.data.order.ListCourierUiModel;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 1/3/18. Tokopedia
 */

public class OrderCourierInteractorImpl implements OrderCourierInteractor {

    private CompositeSubscription compositeSubscription;
    private OrderCourierRepository repository;
    private OrderDetailMapper mapper;

    public OrderCourierInteractorImpl(CompositeSubscription compositeSubscription,
                                      OrderCourierRepository repository, OrderDetailMapper mapper) {
        this.compositeSubscription = compositeSubscription;
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void onGetCourierList(String selectedCourierId,
                                 Map<String, String> params,
                                 Subscriber<ListCourierUiModel> subscriber) {
        compositeSubscription.add(repository.onOrderCourierRepository(selectedCourierId, params)
                .map(courierResponse -> mapper.getCourierServiceModel(courierResponse,
                        selectedCourierId
                ))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void confirmShipping(Map<String, String> params,
                                Subscriber<String> subscriber) {
        compositeSubscription.add(repository.processShipping(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void changeCourier(Map<String, String> params,
                              Subscriber<String> subscriber) {
        compositeSubscription.add(repository.changeCourier(params)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }
}
