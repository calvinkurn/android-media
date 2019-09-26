package com.tokopedia.withdraw.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public class WithdrawSuccessPageContract {

    public interface  View extends CustomerView {
        void setSuccessTtlTxt(String ttlTxt);
        void setBankName(String bankName);
        void setAccountNumber(String accountNumber);
        void setWithdrawAmount(String withdrawAmount);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void executeSuccessUseCase(Object withdrawFormData);
    }
}
