package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.core.database.model.category.CategoryData;
import com.tokopedia.core.database.recharge.recentOrder.LastOrder;
import com.tokopedia.core.database.recharge.status.Status;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;

import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ricoharisin on 7/11/16.
 *         Modified by kulomady on 08/23/2016
 *         Modified by Nabilla Sabbaha on 08/07/2017
 *         Modified by rizkyfadillah at 10/6/17.
 */
public class RechargeNetworkInteractorImpl implements RechargeNetworkInteractor {

    private CompositeSubscription compositeSubscription;
    private DigitalWidgetRepository repository;

    public RechargeNetworkInteractorImpl(DigitalWidgetRepository repository) {
        compositeSubscription = new CompositeSubscription();
        this.repository = repository;
    }

//    @Override
//    public void getRecentNumbers(Subscriber<Boolean> subscriber, Map<String, String> params) {
//        compositeSubscription.add(
//                repository.storeObservableRecentDataNetwork(params)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.newThread())
//                        .unsubscribeOn(Schedulers.newThread())
//                        .subscribe(subscriber));
//    }

//    @Override
//    public void getLastOrder(Subscriber<LastOrder> subscriber, Map<String, String> params) {
//        compositeSubscription.add(
//                repository.getObservableLastOrderNetwork(params)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeOn(Schedulers.newThread())
//                        .unsubscribeOn(Schedulers.newThread())
//                        .subscribe(subscriber));
//    }

    @Override
    public void getCategoryData(Subscriber<CategoryData> subscriber) {
        compositeSubscription.add(
                repository.getObservableCategoryData()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getStatus(Subscriber<Status> subscriber) {
        compositeSubscription.add(
                repository.getObservableStatus()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getStatusResume(Subscriber<Status> subscriber) {
        compositeSubscription.add(
                repository.getObservableStatusOnResume()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null)
            compositeSubscription.unsubscribe();
    }
}