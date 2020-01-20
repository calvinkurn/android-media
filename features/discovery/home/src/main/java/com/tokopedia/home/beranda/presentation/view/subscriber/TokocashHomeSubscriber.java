package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.common_wallet.balance.view.WalletBalanceModel;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeHeaderWalletAction;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by meta on 16/07/18.
 */
public class TokocashHomeSubscriber extends Subscriber<WalletBalanceModel> {

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
    public void onNext(WalletBalanceModel walletBalanceModel) {
        HomeHeaderWalletAction actionWallet = mapper(walletBalanceModel);
        presenter.updateHeaderTokoCashData(actionWallet);
        if (actionWallet.isShowAnnouncement()) {
            presenter.showPopUpIntroWalletOvo(actionWallet.getAppLinkActionButton());
        }
    }

    private HomeHeaderWalletAction mapper(WalletBalanceModel walletBalanceModel) {
        HomeHeaderWalletAction data = new HomeHeaderWalletAction();
        data.setLinked(walletBalanceModel.getLink());
        data.setBalance(walletBalanceModel.getBalance());
        data.setLabelTitle(walletBalanceModel.getTitleText());
        data.setAppLinkBalance(walletBalanceModel.getApplinks());
        data.setLabelActionButton(walletBalanceModel.getActionBalanceModel().getLabelAction());
        data.setVisibleActionButton(walletBalanceModel.getActionBalanceModel().getVisibility() != null
                && walletBalanceModel.getActionBalanceModel().getVisibility().equals("1"));
        data.setAppLinkActionButton(walletBalanceModel.getActionBalanceModel().getApplinks());
        data.setAbTags(walletBalanceModel.getAbTags() == null ? new ArrayList<String>()
                : walletBalanceModel.getAbTags());
        data.setPointBalance(walletBalanceModel.getPointBalance());
        data.setRawPointBalance(walletBalanceModel.getRawPointBalance());
        data.setCashBalance(walletBalanceModel.getCashBalance());
        data.setRawCashBalance(walletBalanceModel.getRawCashBalance());
        data.setWalletType(walletBalanceModel.getWalletType());
        data.setShowAnnouncement(walletBalanceModel.isShowAnnouncement());
        return data;
    }
}
