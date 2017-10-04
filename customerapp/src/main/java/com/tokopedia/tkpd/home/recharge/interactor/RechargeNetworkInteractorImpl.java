package com.tokopedia.tkpd.home.recharge.interactor;

import com.tokopedia.digital.widget.domain.DigitalWidgetRepository;
import com.tokopedia.digital.widget.model.category.Category;
import com.tokopedia.digital.widget.model.lastorder.LastOrder;
import com.tokopedia.digital.widget.model.mapper.CategoryMapper;
import com.tokopedia.digital.widget.model.mapper.LastOrderMapper;
import com.tokopedia.digital.widget.model.mapper.StatusMapper;
import com.tokopedia.digital.widget.model.status.Status;

import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author ricoharisin on 7/11/16.
 *         Modified by kulomady on 08/23/2016
 *         Modified by Nabilla Sabbaha on 08/07/2017
 */
public class RechargeNetworkInteractorImpl implements RechargeNetworkInteractor {

    private CompositeSubscription compositeSubscription;
    private DigitalWidgetRepository repository;
    private LastOrderMapper lastOrderMapper;
    private CategoryMapper categoryMapper;
    private StatusMapper statusMapper;

    public RechargeNetworkInteractorImpl(DigitalWidgetRepository repository,
                                         LastOrderMapper lastOrderMapper,
                                         CategoryMapper categoryMapper,
                                         StatusMapper statusMapper) {
        compositeSubscription = new CompositeSubscription();
        this.repository = repository;
        this.lastOrderMapper = lastOrderMapper;
        this.categoryMapper = categoryMapper;
        this.statusMapper = statusMapper;
    }

    @Override
    public void getRecentNumbers(Subscriber<Boolean> subscriber, Map<String, String> params) {
        compositeSubscription.add(
                repository.storeObservableRecentDataNetwork(params)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getLastOrder(Subscriber<LastOrder> subscriber, Map<String, String> params) {
        compositeSubscription.add(
                repository.getObservableLastOrderNetwork(params)
                        .map(lastOrderMapper)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getCategoryData(Subscriber<List<Category>> subscriber) {
        compositeSubscription.add(
                repository.getObservableCategoryData()
                        .map(categoryMapper)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribe(subscriber));
    }

    @Override
    public void getStatus(Subscriber<Status> subscriber) {
        compositeSubscription.add(
                repository.getObservableStatus()
                        .map(statusMapper)
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