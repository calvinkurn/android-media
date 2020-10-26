package com.tokopedia.changephonenumber.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.changephonenumber.view.uimodel.WarningUIModel;

/**
 * Created by milhamj on 18/12/17.
 */

public interface ChangePhoneNumberWarningFragmentListener {
    public interface View extends CustomerView {
        void showLoading();

        void dismissLoading();

        void onGetWarningSuccess(WarningUIModel warningUIModel);

        void onGetWarningError(String message);

        void onGetValidateOtpStatusSuccess(Boolean isValid);

        void onGetValidateOtpStatusError();

        void goToOvoWebView(String url);

        Integer getUserId();

        Context getContext();
    }

    public interface Presenter extends CustomerPresenter<View> {
        void initView();

        void getWarning();

        void validateOtpStatus(int userId);
    }
}
