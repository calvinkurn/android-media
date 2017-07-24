package com.tokopedia.core.manage.people.bank.presenter;

import com.tokopedia.core.manage.people.bank.activity.ListBankTypeActivity;
import com.tokopedia.core.manage.people.bank.domain.BcaOneClickRepository;
import com.tokopedia.core.manage.people.bank.listener.ListBankTypeActivityView;
import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by kris on 7/24/17. Tokopedia
 */

public class ListBankTypePresenterImpl implements ListBankTypePresenter {

    private ListBankTypeActivityView mainView;
    private CompositeSubscription compositeSubscription;
    private BcaOneClickRepository bcaOneClickRepository;

    public ListBankTypePresenterImpl(ListBankTypeActivityView view) {
        mainView = view;
        BcaOneClickService bcaOneClickService = new BcaOneClickService();
        compositeSubscription = new CompositeSubscription();
        bcaOneClickRepository = new BcaOneClickRepository(bcaOneClickService);
    }

    @Override
    public void onOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber) {
        compositeSubscription.add(bcaOneClickRepository.getBcaOneClickAccessToken()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }

    @Override
    public void onDestroyed() {
        compositeSubscription.unsubscribe();
    }
}
