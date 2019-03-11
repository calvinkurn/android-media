package com.tokopedia.ovo.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.ovo.model.ImeiConfirmResponse;
import com.tokopedia.ovo.model.Wallet;

public class PaymentQrSummaryContract {
    public interface Presenter extends CustomerPresenter<View> {
        void confirmQrPayment(String imeiNumber, int transferId, float amount, float fees, boolean showUsePointToggle);
    }

    public interface View extends CustomerView {
        void setWalletBalance(Wallet wallet);

        void goToUrl(ImeiConfirmResponse response);

        void showError(String message);
    }
}
