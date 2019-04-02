package com.tokopedia.gamification.floating.view.contract;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.gamification.data.entity.TokenDataEntity;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public interface FloatingEggContract {

    interface View extends CustomerView {
        void onSuccessGetToken(TokenDataEntity tokenData);

        void onErrorGetToken(Throwable throwable);

        Resources getResources();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getGetTokenTokopoints();

        boolean isUserLogin();
    }
}
