package com.tokopedia.posapp.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletItemViewModel;
import com.tokopedia.posapp.view.viewmodel.outlet.OutletViewModel;

import java.util.List;

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

    interface Presenter extends CustomerPresenter<Outlet.View> {
        void getOutlet(String query);

        void setHasNextPage(String uriNext);

        void getNextOutlet(String query);
    }
}
