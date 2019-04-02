package com.tokopedia.pms.bankdestination.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.pms.bankdestination.view.model.BankListModel;

/**
 * Created by zulfikarrahman on 7/5/18.
 */

public interface BankDestinationContract {
    interface View extends BaseListViewListener<BankListModel>{

    }

    interface Presenter extends CustomerPresenter<View> {
        void getListBank();
    }
}
