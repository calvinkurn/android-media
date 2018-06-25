package com.tokopedia.paymentmanagementsystem.paymentlist.view.presenter;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 6/21/18.
 */

public interface PaymentListContract {
    interface View extends BaseListViewListener{

    }

    interface Presenter extends CustomerPresenter<View>{

    }
}
