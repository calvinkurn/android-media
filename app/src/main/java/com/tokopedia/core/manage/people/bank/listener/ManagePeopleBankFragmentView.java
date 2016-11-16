package com.tokopedia.core.manage.people.bank.listener;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.core.manage.people.bank.adapter.BankAdapter;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFragment;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created by Nisie on 6/10/16.
 */
public interface ManagePeopleBankFragmentView {
    Activity getActivity();

    void setActionsEnabled(boolean isEnabled);

    void showLoading();

    BankAdapter getAdapter();

    void showEmptyState(String message, NetworkErrorHelper.RetryClickedListener listener);

    void showSnackbar(String message, NetworkErrorHelper.RetryClickedListener listener);

    void finishLoading();

    void setOnActionBankListener(ManagePeopleBankFragment.BankFormListener listener);

    void onSuccessEditDefaultBankAccount(Bundle resultData);

    void onSuccessDeleteBankAccount(Bundle resultData);

    void onFailedEditDefaultBankAccount(Bundle resultData);

    void onFailedDeleteBankAccount(Bundle resultData);

    void refresh();

    void showDialogLoading();

    ManagePeopleBankFragment.BankFormListener getBankFormListener();
}
