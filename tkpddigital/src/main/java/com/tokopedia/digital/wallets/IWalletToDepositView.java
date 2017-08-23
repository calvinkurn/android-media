package com.tokopedia.digital.wallets;

import com.tokopedia.digital.cart.listener.IBaseView;

/**
 * @author anggaprasetiyo on 8/21/17.
 */

public interface IWalletToDepositView extends IBaseView {
    void renderSomethingFromGet(WalletToDepositData data);

    void renderSomethingFromProcess(WalletToDepositThanksData data);

    String getTitlePageFromPassData();

    int getWalletAmountFromPassData();

    String getWalletAmountFormattedFromPassData();
}
