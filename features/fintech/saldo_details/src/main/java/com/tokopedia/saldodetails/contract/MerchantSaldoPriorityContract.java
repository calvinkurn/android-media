package com.tokopedia.saldodetails.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

public interface MerchantSaldoPriorityContract {

    interface View extends CustomerView {
        void showProgressLoading();

        void hideProgressLoading();

        void showErrorMessage();

    }

    interface Presenter extends CustomerPresenter<View> {
        void onDestroyView();
    }


}


