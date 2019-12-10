package com.tokopedia.useridentification.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.useridentification.subscriber.GetUserProjectInfoSubcriber;

/**
 * @author by alvinatin on 08/11/18.
 */

public interface UserIdentificationInfo {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        GetUserProjectInfoSubcriber.GetUserProjectInfoListener getUserProjectInfoListener();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getInfo(int projectId);
    }
}
