package com.tokopedia.core.talk.talkproduct.interactor;

import com.tokopedia.core.network.apiservices.kunyit.KunyitService;
import com.tokopedia.core.talk.talkproduct.presenter.TalkAddNewPresenter;
import com.tokopedia.core.talk.talkproduct.presenter.TalkAddNewPresenterImpl;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by stevenfredian on 8/2/16.
 */
public class TalkAddNewRetrofitInteractorImpl implements TalkAddNewRetrofitInteractor{


    CompositeSubscription compositeSubscription;
    KunyitService kunyitService;
    TalkAddNewPresenter presenter;


    public static TalkAddNewRetrofitInteractor createInstance(TalkAddNewPresenterImpl talkAddNewPresenter) {
        TalkAddNewRetrofitInteractorImpl facade = new TalkAddNewRetrofitInteractorImpl();
        facade.presenter = talkAddNewPresenter;
        facade.kunyitService = new KunyitService();
        facade.compositeSubscription = new CompositeSubscription();
        return facade;
    }

    @Override
    public void unSubscribe() {
        compositeSubscription.unsubscribe();
    }
}
