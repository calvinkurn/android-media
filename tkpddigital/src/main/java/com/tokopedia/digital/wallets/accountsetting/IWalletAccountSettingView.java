package com.tokopedia.digital.wallets.accountsetting;

import com.tokopedia.digital.cart.listener.IBaseView;

/**
 * @author anggaprasetiyo on 8/24/17.
 */

public interface IWalletAccountSettingView extends IBaseView {

    void renderWalletAccountSettingData(WalletAccountSettingData walletAccountSettingData);

    void renderErrorGetWalletAccountSettingData(String message);

    void renderErrorHttpGetWalletAccountSettingData(String message);

    void renderErrorNoConnectionGetWalletAccountSettingData(String message);

    void renderErrorTimeoutConnectionGetWalletAccountSettingData(String message);

    void disableSwipeRefresh();

    void enableSwipeRefresh();
}
