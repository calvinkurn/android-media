package com.tokopedia.home.account.presentation.subscriber;

import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author okasurya on 8/23/18.
 */
public class GetBuyerAccountSubscriber extends BaseAccountSubscriber<BuyerViewModel> {
    private BuyerAccount.View view;

    public GetBuyerAccountSubscriber(BuyerAccount.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onNext(BuyerViewModel buyerViewModel) {
        view.loadBuyerData(buyerViewModel);
        view.hideLoading();
    }
}
