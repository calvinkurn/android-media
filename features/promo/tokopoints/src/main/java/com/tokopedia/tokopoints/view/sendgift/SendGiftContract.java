package com.tokopedia.tokopoints.view.sendgift;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;

public interface SendGiftContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void showLoadingSendNow();

        void hideLoadingSendNow();

        void openPreConfirmationWindow();

        void onErrorPreValidate(String error);

        void onSuccess();

        void onError(String error);

        Context getAppContext();

        Context getActivityContext();

        void showPopup(String title, String message, int success);
    }

    interface Presenter  {

        void sendGift(Integer id, String email, String notes);

        void preValidateGift(Integer id, String email);
    }
}
