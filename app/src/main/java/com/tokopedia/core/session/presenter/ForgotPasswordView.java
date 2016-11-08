package com.tokopedia.core.session.presenter;

import com.tokopedia.core.presenter.BaseView;

/**
 * @author m.normansyah
 * @since 18/11/2015.
 * @version 2
 */
public interface ForgotPasswordView extends BaseView {
    void setLocalyticFlow();
    void displaySuccessView(boolean isSuccess);
    void displayFrontView(boolean isFrontView);
    void setTextEmailSend(String text);
    void moveToRegister(String email);

    void showProgressDialog();

    void dismissProgressDialog();

    boolean checkIsLoading();

    void setIsProgressDialog(boolean aBoolean);
}
