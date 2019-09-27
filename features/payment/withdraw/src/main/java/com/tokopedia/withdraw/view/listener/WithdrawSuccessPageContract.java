package com.tokopedia.withdraw.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.HashMap;

public class WithdrawSuccessPageContract {

    public interface  View extends CustomerView {
        void setSuccessTtlTxt(String ttlTxt);
        void setBankName(String bankName);
        void setAccountNumber(String accountNumber);
        void setWithdrawAmount(String withdrawAmount);
    }

    public interface Presenter extends CustomerPresenter<View> {
        void executeSuccessUseCase(HashMap<String, Object> withdrawFormData, Context context);
    }
}
