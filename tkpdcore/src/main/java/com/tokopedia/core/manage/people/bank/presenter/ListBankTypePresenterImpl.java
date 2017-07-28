package com.tokopedia.core.manage.people.bank.presenter;

import com.tokopedia.core.manage.people.bank.activity.ListBankTypeActivity;
import com.tokopedia.core.manage.people.bank.domain.BcaOneClickRepository;
import com.tokopedia.core.manage.people.bank.listener.ListBankTypeActivityView;
import com.tokopedia.core.manage.people.bank.model.BcaOneClickData;
import com.tokopedia.core.network.apiservices.payment.BcaOneClickService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;

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

    /*@Override
    public void onOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber) {
        TKPDMapParam<String, String> bcaOneClickParam = new TKPDMapParam<>();
        bcaOneClickParam.put("grant_type", "client_credentials");
        compositeSubscription.add(bcaOneClickRepository.accessBcaOneClick(AuthUtil
                .generateParamsNetwork(mainView.getContext() ,bcaOneClickParam))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.newThread())
                .subscribe(subscriber));
    }*/

    @Override
    public void onOneClickBcaChosen(Subscriber<BcaOneClickData> subscriber) {
        TKPDMapParam<String, String> bcaOneClickParam = new TKPDMapParam<>();
        bcaOneClickParam.put("tokopedia_user_id", SessionHandler.getLoginID(mainView.getContext()));
        bcaOneClickParam.put("merchant_code", "tokopedia");
        bcaOneClickParam.put("action", "auth");
        bcaOneClickParam.put("profile_code", "TKPD_DEFAULT");
        compositeSubscription.add(bcaOneClickRepository.getBcaOneClickAccessToken(AuthUtil
                .generateParamsNetwork(mainView.getContext() ,bcaOneClickParam))
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
