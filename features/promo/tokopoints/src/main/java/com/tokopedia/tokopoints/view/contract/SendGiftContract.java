package com.tokopedia.tokopoints.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

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

        void showPopup(String title, String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void sendGift(int id, String email, String notes);

        void preValidateGift(int id, String email);
    }
}
