package com.tokopedia.paymentmanagementsystem.changebankaccount.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public interface ChangeBankAccountContract {
    interface View extends CustomerView{

    }
    interface Presenter extends CustomerPresenter<View>{

    }
}
