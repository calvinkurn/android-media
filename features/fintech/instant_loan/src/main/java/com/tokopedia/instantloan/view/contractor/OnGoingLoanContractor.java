package com.tokopedia.instantloan.view.contractor;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface OnGoingLoanContractor {

    interface View extends CustomerView {
        void setUserOnGoingLoanStatus(boolean status, int id);
    }

    interface Presenter extends CustomerPresenter<OnGoingLoanContractor.View> {
        void checkUserOnGoingLoanStatus();
    }

}
