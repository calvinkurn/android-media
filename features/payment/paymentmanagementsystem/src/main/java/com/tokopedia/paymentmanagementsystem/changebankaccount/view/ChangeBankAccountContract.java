package com.tokopedia.paymentmanagementsystem.changebankaccount.view;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public interface ChangeBankAccountContract {
    interface View extends CustomerView{

        void onErrorEditDetailAccount(Throwable e);

        void onResultEditDetailAccount(boolean success);
    }
    interface Presenter extends CustomerPresenter<View>{
        void saveDetailAccount(Resources resources, String transactionId, String merchantCode, String destbank, String accountNo, String accountName, String notes);
    }
}
