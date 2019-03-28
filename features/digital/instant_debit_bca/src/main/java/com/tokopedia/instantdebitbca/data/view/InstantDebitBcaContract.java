package com.tokopedia.instantdebitbca.data.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 25/03/19.
 */
public interface InstantDebitBcaContract {

    interface View extends CustomerView {
        void openWidgetBca(String accessToken);

        void redirectPageAfterRegisterBca();

        void showErrorMessage(Throwable throwable);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getAccessTokenBca();

        void notifyDebitRegisterBca(String debitData, String deviceId);

        void onDestroy();
    }
}
