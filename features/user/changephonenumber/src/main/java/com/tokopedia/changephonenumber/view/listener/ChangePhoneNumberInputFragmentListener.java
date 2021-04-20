package com.tokopedia.changephonenumber.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by milhamj on 20/12/17.
 */

public interface ChangePhoneNumberInputFragmentListener {
    public interface View extends CustomerView {
        void enableNextButton();

        void disableNextButton();

        void correctPhoneNumber(String newNumber, int selection);

        void showLoading();

        void dismissLoading();

        void onValidateNumberSuccess();

        void onValidateNumberError(String message);

        void onSubmitNumberSuccess();

        void onSubmitNumberError(String message);

        Context getContext();
    }

    public interface Presenter extends CustomerPresenter<View> {

        void onNewNumberTextChanged(String phoneNumber, int selection);

        void validateNumber(String newPhoneNumber);

        void submitNumber(String newPhoneNumber);
    }
}
