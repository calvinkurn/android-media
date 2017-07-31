package com.tokopedia.posapp.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;

import java.util.List;

/**
 * Created by okasurya on 7/31/17.
 */

public interface Outlet {
    interface View extends CustomerView {
        void onOutletClicked(String outletId);

        void onSuccessGetOutlet(List<OutletViewModel> outlets);

        void onErrorGetOutlet(String errorMessage);

        void showLoading();

        void hideLoading();

    }

    interface Presenter extends CustomerPresenter<View> {
        void getOutlet(RequestParams params);
    }
}
