package com.tokopedia.tkpd.tokocash;

import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

import rx.functions.Func1;

/**
 * @author okasurya on 9/4/18.
 */
class TokoCashAccountBalanceMapper implements Func1<BalanceTokoCash, WalletModel> {
    @Override
    public WalletModel call(BalanceTokoCash balanceTokoCash) {
        WalletModel walletModel = new WalletModel();
        walletModel.setApplink(balanceTokoCash.getApplinks());
        walletModel.setText(balanceTokoCash.getTitleText());
        walletModel.setBalance(balanceTokoCash.getBalance());
        return walletModel;
    }
}
