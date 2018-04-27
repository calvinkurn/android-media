package com.tokopedia.gamification.floating.view.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.gamification.floating.view.model.TokenData;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public interface FloatingEggContract {

    interface View extends CustomerView {
        void onSuccessGetToken(TokenData tokenData);

        void onErrorGetToken(Throwable throwable);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getGetTokenTokopoints();

        boolean isUserLogin();
    }
}
