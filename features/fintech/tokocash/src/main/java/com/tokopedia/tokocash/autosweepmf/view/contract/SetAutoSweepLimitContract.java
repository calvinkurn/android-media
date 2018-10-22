package com.tokopedia.tokocash.autosweepmf.view.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;

public interface SetAutoSweepLimitContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void onSuccessAccountStatus(AutoSweepLimit data);

        void onErrorAccountStatus(String error);

        Context getAppContext();

        Context getActivityContext();

        Long getLongRemoteConfig(String key);
    }

    interface Presenter extends CustomerPresenter<View> {
        void destroyView();

        void postAutoSweepLimit(boolean isEnable, int amount);
    }
}
