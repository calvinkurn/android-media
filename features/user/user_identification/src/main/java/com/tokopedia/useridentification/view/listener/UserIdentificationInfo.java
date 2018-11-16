package com.tokopedia.useridentification.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.user_identification_common.subscriber.GetApprovalStatusSubscriber;

/**
 * @author by alvinatin on 08/11/18.
 */

public interface UserIdentificationInfo {

    interface View extends CustomerView {

        void showLoading();

        void hideLoading();

        GetApprovalStatusSubscriber.GetApprovalStatusListener getApprovalStatusListener();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getStatus();
    }
}
