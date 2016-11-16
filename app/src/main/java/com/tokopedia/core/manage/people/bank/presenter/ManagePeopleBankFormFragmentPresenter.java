package com.tokopedia.core.manage.people.bank.presenter;


import com.tokopedia.core.database.model.Bank;

import java.util.List;

/**
 * Created by Nisie on 6/13/16.
 */
public interface ManagePeopleBankFormFragmentPresenter {
    void onSaveClicked();

    List<Bank> getListBankFromDB(String query);

    void sendOTP();
}
