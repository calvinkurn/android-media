package com.tokopedia.useridentification.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by alvinatin on 08/11/18.
 */

public interface UserIdentificationInfo {

    interface View extends CustomerView{

        void onSuccessGetInfo(int status);

        void onErrorGetInfo();

        void showLoading();

        void hideLoading();
    }

    interface Presenter extends CustomerPresenter<View>{
        void getStatus();
    }
}
