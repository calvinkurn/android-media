package com.tokopedia.promogamification.common.floating.view.contract;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.promogamification.common.floating.data.entity.GamiFloatingButtonEntity;
import com.tokopedia.promogamification.common.floating.data.entity.GamiFloatingClickData;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public interface FloatingEggContract {

    interface View extends CustomerView {
        void onSuccessGetToken(GamiFloatingButtonEntity gamiFloatingButtonEntity);
        void onSuccessClickClose(GamiFloatingClickData gamiFloatingClickData);

        void onErrorGetToken(Throwable throwable);
        void onErrorClickClose(Throwable throwable);

        Resources getResources();

    }

    interface Presenter extends CustomerPresenter<View> {
        void getGetTokenTokopoints();

        boolean isUserLogin();

        void clickCloseButton(int floatingId);
    }
}
