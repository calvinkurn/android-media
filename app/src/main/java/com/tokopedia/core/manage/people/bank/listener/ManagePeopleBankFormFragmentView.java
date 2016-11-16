package com.tokopedia.core.manage.people.bank.listener;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.manage.people.bank.activity.ManagePeopleBankActivity;
import com.tokopedia.core.manage.people.bank.fragment.ManagePeopleBankFormFragment;

/**
 * Created by Nisie on 6/13/16.
 */
public interface ManagePeopleBankFormFragmentView {
    Context getActivity();

    void removeError();

    void finishLoading();

    EditText getPassword();

    EditText getOTP();

    EditText getAccountName();

    EditText getAccountNumber();

    void setError(String error);

    TextView getBankName();

    EditText getBranchName();

    TextInputLayout getBranchNameWrapper();

    TextInputLayout getAccountNumberWrapper();

    TextInputLayout getAccountNameWrapper();

    TextInputLayout getOTPWrapper();

    TextInputLayout getPasswordWrapper();

    String getBankId();

    void notifyError(TextInputLayout wrapper, String error);

    void showDialogLoading();

    void setActionsEnabled(boolean isEnabled);

    void onSuccessAddBankAccount(Bundle resultData);

    void onSuccessEditBankAccount(Bundle resultData);

    void onFailedAddBankAccount(Bundle resultData);

    void onFailedEditBankAccount(Bundle resultData);

    void setOnFinishActionListener(ManagePeopleBankFormFragment.FinishActionListener listener);

    Bundle getArguments();
}
