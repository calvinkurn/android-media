package com.tokopedia.pms.clickbca.view;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public interface ChangeClickBcaContract {
    interface View extends CustomerView{

        void onErrorChangeClickBcaUserID(Throwable e);

        void onResultChangeClickBcaUserId(boolean isSuccess, String message);

        void showLoadingDialog();

        void hideLoadingDialog();
    }

    interface Presenter extends CustomerPresenter<View>{

        void changeClickBcaUserId(Resources resources, String transactionId, String merchantCode, String newClickBcaUserId);
    }
}
