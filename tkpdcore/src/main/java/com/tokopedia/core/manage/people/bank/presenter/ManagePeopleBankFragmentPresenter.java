package com.tokopedia.core.manage.people.bank.presenter;

import com.tokopedia.core.manage.people.bank.model.ActSettingBankPass;
import com.tokopedia.core.manage.people.bank.model.BankAccount;

/**
 * Created by Nisie on 6/10/16.
 */
public interface ManagePeopleBankFragmentPresenter {
    void initData();

    void onDestroyView();

    void onEditBank(ActSettingBankPass bankAccount);

    void onDeleteBank(ActSettingBankPass bankAccount);

    void onDefaultBank(ActSettingBankPass bankAccount);
}
