package com.tokopedia.saldodetails.contract;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface MerchantSaldoPriorityContract {

    interface View extends CustomerView {
        void showProgressLoading();

        void hideProgressLoading();

        Context getContext();

        void onSaldoStatusUpdateError(String errorMessage);

        void onSaldoStatusUpdateSuccess(boolean newState);
    }

    interface Presenter extends CustomerPresenter<View> {
        void onDestroyView();

        void updateSellerSaldoStatus(boolean value);
    }
}


