package com.tokopedia.digital.wallets.accountsetting;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public class WalletAccountSettingPresenter implements IWalletAccountSettingPresenter {

    private final IWalletAccountSettingView view;
    private final Object interactor; //TODO bikin interactor


    public WalletAccountSettingPresenter(IWalletAccountSettingView view, Object interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void processGetWalletAccountData() {

    }

    @Override
    public void processDeleteConnectedUser() {

    }
}
