package com.tokopedia.home.beranda.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.CashBackData;
import com.tokopedia.home.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tokocash.pendingcashback.domain.PendingCashback;

import java.util.List;

import rx.Observable;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {

        boolean isLoading();

        void showLoading();

        void hideLoading();

        void setItems(List<Visitable> items);

        void updateHeaderItem(HeaderViewModel headerViewModel);

        void showNetworkError(String message);

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        Activity getActivity();

        void updateListOnResume(List<Visitable> visitables);

        void showRecomendationButton();

        Observable<TokoCashData> getTokocashBalance();

        Observable<PendingCashback> getTokocashPendingCashback();

        Observable<TokoPointDrawerData> getTokopoint();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getHomeData();

        void updateHomeData();

        void getHeaderData(boolean initialStart);

        void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletActionData);

        void updateHeaderTokoCashPendingData(CashBackData cashBackData);

        void updateHeaderTokoPointData(TokoPointDrawerData tokoPointDrawerData);

        void getShopInfo(String url, String shopDomain);

        void openProductPageIfValid(String url, String shopDomain);

        void onHeaderTokocashError();

        void onHeaderTokopointError();

        void onRefreshTokoPoint();

        void onRefreshTokoCash();

        void onResume();

        void onFirstLaunch();

        void onDestroy();
    }
}
