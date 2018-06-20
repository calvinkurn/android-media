package com.tokopedia.tokocash.historytokocash.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.historytokocash.presentation.model.ParamsActionHistory;

/**
 * Created by nabillasabbaha on 2/19/18.
 */

public interface MoveToSaldoContract {

    interface View extends CustomerView {
        void wrappingDataSuccess(long amount);

        void wrappingDataFailed();

        void showProgressLoading();

        void hideProgressLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void processMoveToSaldo(String url, ParamsActionHistory paramsActionHistory);

        void destroyView();
    }
}
