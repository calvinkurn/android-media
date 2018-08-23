package com.tokopedia.home.account.presentation.subscriber;

import com.tokopedia.home.account.presentation.SellerAccount;
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author okasurya on 8/23/18.
 */
public class GetSellerAccountSubscriber extends Subscriber<SellerViewModel> {
    private SellerAccount.View view;

    public GetSellerAccountSubscriber(SellerAccount.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof UnknownHostException) {
            view.showErroNoConnection();
        } else {
            view.showError(throwable.getLocalizedMessage());
        }

        view.hideLoading();
    }

    @Override
    public void onNext(SellerViewModel model) {
        view.loadSellerData(model);
        view.hideLoading();
    }
}
