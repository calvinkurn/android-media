package com.tokopedia.core.deposit.listener;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tokopedia.core.deposit.adapter.BankAdapter;
import com.tokopedia.core.deposit.model.WithdrawForm;
import com.tokopedia.core.network.NetworkErrorHelper;

/**
 * Created by Nisie on 4/15/16.
 */
public interface WithdrawFragmentView {
    void showLoading();

    BankAdapter getAdapter();

    void setForm(WithdrawForm data);

    void removeError();

    EditText getPassword();

    EditText getOTP();

    View getBankForm();

    EditText getAccountName();

    EditText getAccountNumber();

    Spinner getBankList();

    void setError(String string);

    EditText getTotalWithdrawal();

    TextView getBankName();

    EditText getBranchName();

    View getOTPArea();

    void finishLoading();

    void enableView();

    void disableView();

    TextInputLayout getTotalWithdrawalWrapper();

    TextInputLayout getBranchNameWrapper();

    TextInputLayout getAccountNumberWrapper();

    TextInputLayout getAccountNameWrapper();

    TextInputLayout getOTPWrapper();

    TextInputLayout getPasswordWrapper();

    String getBankId();

    Activity getActivity();

    String  getString(int resId);

    void notifyError(TextInputLayout wrapper, String errorMsg);

    Bundle getArguments();

    void showProgressDialog();

    void showEmptyState(NetworkErrorHelper.RetryClickedListener listener);

    void showEmptyState(String message,NetworkErrorHelper.RetryClickedListener listener);

    void setRetry(NetworkErrorHelper.RetryClickedListener listener);

    void setRetry(String error, NetworkErrorHelper.RetryClickedListener listener);
}
