package com.tokopedia.tokopoints.view.contract;

import android.content.Context;
import android.support.annotation.IdRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusTierEntity;

import java.util.List;

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
