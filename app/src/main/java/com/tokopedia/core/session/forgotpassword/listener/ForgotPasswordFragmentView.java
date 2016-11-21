package com.tokopedia.core.session.forgotpassword.listener;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Alifa on 10/17/2016.
 */

public interface ForgotPasswordFragmentView {
    Context getContext();

    void finishLoading();

    void setLoading();

    void showErrorMessage();

    void removeError();

    void setActionsEnabled(Boolean isEnabled);

    Activity getActivity();

    String getString(int resId);

    void resetPassword();

    void refresh();
}
