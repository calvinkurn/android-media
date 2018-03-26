package com.tokopedia.posapp.outlet.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.posapp.outlet.view.viewmodel.OutletItemViewModel;
import com.tokopedia.posapp.outlet.view.viewmodel.OutletViewModel;

/**
 * Created by okasurya on 7/31/17.
 */

public interface Outlet {
    interface View extends CustomerView {
        void clearOutletData();

        void onOutletClicked(OutletItemViewModel outletItem);

        void onSuccessGetOutlet(OutletViewModel outlet);

        void onErrorGetOutlet(String errorMessage);

        void startLoading();

        void finishLoading();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getOutlet(String query);

        void setHasNextPage(String uriNext);

        void getNextOutlet(String query);
    }
}
