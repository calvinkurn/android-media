package com.tokopedia.tkpd.talk.talkproduct.interactor;

import com.tokopedia.tkpd.network.apiservices.kunyit.KunyitService;
import com.tokopedia.tkpd.network.apiservices.product.TalkActService;
import com.tokopedia.tkpd.talk.talkproduct.presenter.TalkAddNewPresenter;
import com.tokopedia.tkpd.talk.talkproduct.presenter.TalkAddNewPresenterImpl;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 8/2/16.
 */
public class TalkAddNewRetrofitInteractorImpl implements TalkAddNewRetrofitInteractor{


    CompositeSubscription compositeSubscription;
    TalkActService talkActService;
    KunyitService kunyitService;
    TalkAddNewPresenter presenter;


    public static TalkAddNewRetrofitInteractor createInstance(TalkAddNewPresenterImpl talkAddNewPresenter) {
        TalkAddNewRetrofitInteractorImpl facade = new TalkAddNewRetrofitInteractorImpl();
        facade.presenter = talkAddNewPresenter;
        facade.talkActService = new TalkActService();
        facade.kunyitService = new KunyitService();
        facade.compositeSubscription = new CompositeSubscription();
        return facade;
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
