package com.tokopedia.tokocash.qrpayment.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 1/22/18.
 */

public interface SuccessQrPaymentContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {
        void deleteCacheTokoCashBalance();
    }
}
