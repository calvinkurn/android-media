package com.tokopedia.paymentmanagementsystem.proof.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.paymentmanagementsystem.proof.model.UploadProof;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public interface UploadProofPaymentContract {
    interface View extends CustomerView{

        void showLoadingDialog();

        void hideLoadingDialog();

        void onErrorUploadProof(Throwable e);

        void onResultUploadProof(UploadProof isSuccess);
    }

    interface Presenter extends CustomerPresenter<View>{

        void uploadProofPayment(String transactionId, String merchantCode, String imageUrl);
    }
}
