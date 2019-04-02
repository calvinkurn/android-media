package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

import rx.Subscriber;

/**
 * Created by meta on 16/07/18.
 */
public class TokocashHomeSubscriber extends Subscriber<HomeHeaderWalletAction> {

    private HomeContract.Presenter presenter;

    public TokocashHomeSubscriber(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        presenter.onHeaderTokocashError();
    }

    @Override
    public void onNext(HomeHeaderWalletAction homeHeaderWalletAction) {
        presenter.updateHeaderTokoCashData(homeHeaderWalletAction);
        if (homeHeaderWalletAction.isShowAnnouncement()) {
            presenter.showPopUpIntroWalletOvo(homeHeaderWalletAction.getAppLinkActionButton());
        }
    }
}
