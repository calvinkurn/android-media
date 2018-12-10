package com.tokopedia.pms.proof.view;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.pms.proof.model.UploadProof;

/**
 * Created by zulfikarrahman on 7/6/18.
 */

public interface UploadProofPaymentContract {
    interface View extends CustomerView{

        void showLoadingDialog();

        void hideLoadingDialog();

        void onErrorUploadProof(Throwable e);

        void onResultUploadProof(UploadProof isSuccess);

        void showLoadingMain();

        void hideLoadingMain();

        void onSuccessGetImageProof(String imageUrl);

        void onErrorGetImageProof(Throwable e);
    }

    interface Presenter extends CustomerPresenter<View>{

        void uploadProofPayment(String transactionId, String merchantCode, String imageUrl);

        void getProofPayment(String transactionId, String merchantCode, Resources resources);
    }
}
