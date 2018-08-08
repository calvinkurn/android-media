package com.tokopedia.core.manage.people.profile.listener;

import android.app.Activity;
import android.widget.EditText;

import com.tokopedia.core.manage.people.profile.fragment.EmailVerificationDialogFragment;

/**
 * Created by nisie on 9/29/16.
 */

public interface EmailVerificationView {
    void showLoadingProgress();

    void onSuccessRequestOTP();

    void finishLoading();

    void showErrorToast(String error);

    Activity getActivity();

    EditText getEmail();

    String getString(int resId);

    EditText getPassword();

    EditText getOTP();

    void onSuccessChangeEmail();

    void setEmailChangeListener(EmailVerificationDialogFragment.EmailChangeConfirmationListener listener);
}
