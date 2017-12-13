package com.tokopedia.tkpd.home.presenter;

import com.tokopedia.digital.tokocash.mapper.TokoCashMapper;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.tkpd.home.interactor.TokoCashHomeInteractor;
import com.tokopedia.tkpd.home.interactor.TokoCashHomeInteractorImpl;

import rx.Subscriber;

/**
 * Created by kris on 6/15/17. Tokopedia
 */

public class TokoCashPresenterImpl implements TokoCashPresenter {

    private TokoCashHomeInteractor tokoCashHomeInteractor;

    private CategoryView view;

    public TokoCashPresenterImpl(CategoryView categoryView) {
        view = categoryView;
        tokoCashHomeInteractor = new TokoCashHomeInteractorImpl(new TokoCashMapper()
        );
    }

    @Override
    public void onRequestCashBackPending() {
        tokoCashHomeInteractor.requestPendingCashBack(new Subscriber<CashBackData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(CashBackData cashBackData) {
                view.onReceivePendingCashBack(cashBackData);
            }
        });
    }

    @Override
    public void onDestroy() {
        tokoCashHomeInteractor.onDestroy();
    }

}
