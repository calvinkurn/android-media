package com.tokopedia.home.account.presentation.subscriber;

import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author okasurya on 8/23/18.
 */
public class BuyerAccountSubscriber extends Subscriber<BuyerViewModel> {
    private BuyerAccount.View view;

    public BuyerAccountSubscriber(BuyerAccount.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        if(throwable instanceof UnknownHostException) {
            view.showErroNoConnection();
        } else {
            view.showError(throwable.getLocalizedMessage());
        }

        view.hideLoading();
    }

    @Override
    public void onNext(BuyerViewModel buyerViewModel) {
        view.loadBuyerData(buyerViewModel);
        view.hideLoading();
    }
}
