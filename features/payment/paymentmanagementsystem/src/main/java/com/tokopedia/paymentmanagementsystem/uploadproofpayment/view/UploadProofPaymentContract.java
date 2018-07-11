package com.tokopedia.paymentmanagementsystem.uploadproofpayment.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public interface UploadProofPaymentContract {
    interface View extends CustomerView{

    }

    interface Presenter extends CustomerPresenter<View>{

        void uploadProofPayment(String transactionId, String merchantCode, String imageUrl);
    }
}
