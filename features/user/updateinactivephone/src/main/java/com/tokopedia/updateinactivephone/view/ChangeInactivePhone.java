package com.tokopedia.updateinactivephone.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class ChangeInactivePhone {

    public interface View extends CustomerView {

        void showErrorPhoneNumber(int resId);

        void showErrorPhoneNumber(String errorMessage);

        void dismissLoading();

        void showLoading();

        void onForbidden();

        void onPhoneStatusSuccess(String userId);

        void onPhoneRegisteredWithEmail();

        void onPhoneDuplicateRequest();

        void onPhoneServerError();

        void onPhoneBlackListed();

        void onPhoneInvalid();

        void onPhoneNotRegistered();

        void onPhoneTooShort();

        void onPhoneTooLong();

    }

    public interface Presenter extends CustomerPresenter<View> {

        void checkPhoneNumberStatus(String text);
    }
}
