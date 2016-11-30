package com.tokopedia.core.deposit.presenter;


import com.tokopedia.core.database.model.Bank;

import java.util.List;

/**
 * Created by Nisie on 3/30/16.
 */
public interface WithdrawFragmentPresenter {
    void getBankList();

    void doWithdraw();

    void onConfirmClicked();

    void onBankListSelected(int position);

    void sendOTP();

    List<Bank> getListBankFromDB(String s);

    void onDestroyView();

}
