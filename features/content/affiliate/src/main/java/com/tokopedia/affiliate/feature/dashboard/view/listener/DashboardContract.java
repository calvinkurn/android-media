package com.tokopedia.affiliate.feature.dashboard.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardFloatingButtonViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardHeaderViewModel;
import com.tokopedia.affiliate.feature.dashboard.view.viewmodel.DashboardItemViewModel;

import java.util.List;

/**
 * @author by yfsx on 13/09/18.
 */
public interface DashboardContract {

    interface View extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void onSuccessGetDashboardItem(DashboardHeaderViewModel header,
                                       List<DashboardItemViewModel> itemList,
                                       String cursor,
                                       DashboardFloatingButtonViewModel floatingModel);

        void onErrorGetDashboardItem(String error);

        void onSuccessLoadMoreDashboardItem(List<DashboardItemViewModel> itemList, String cursor);

        void onErrorLoadMoreDashboardItem(String error);

        void onSuccessCheckAffiliate(boolean isAffiliate);

        void onErrorCheckAffiliate(String error);

        void goToAffiliateExplore();

        void goToDeposit();
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadDashboardItem(boolean isPullToRefresh);

        void loadMoreDashboardItem(String cursor);

        void checkAffiliate();
    }
}
