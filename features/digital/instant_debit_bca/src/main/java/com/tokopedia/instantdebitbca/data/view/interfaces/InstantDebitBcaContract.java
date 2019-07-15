package com.tokopedia.instantdebitbca.data.view.interfaces;

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

        void createAndSetBcaWidget();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getAccessTokenBca();

        void notifyDebitRegisterBca(String debitData, String deviceId);
        void notifyDebitRegisterEditLimit(String debitData, String deviceId);

        void onDestroy();
    }
}
