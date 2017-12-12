package com.tokopedia.tkpd.beranda.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction;
import com.tokopedia.core.drawer2.data.viewmodel.TokoPointDrawerData;
import com.tokopedia.digital.tokocash.model.CashBackData;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.HeaderViewModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void setItems(List<Visitable> items);

        void setItem(int pos, Visitable item);

        void updateHeaderItem(HeaderViewModel headerViewModel);

        void refreshAdapter();

        void showNetworkError(String message);

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        Activity getActivity();

        void onGimickItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

    }

    interface Presenter extends CustomerPresenter<View> {
        void getHomeData();

        void updateHeaderTokoCashData(HomeHeaderWalletAction homeHeaderWalletActionData);

        void updateHeaderTokoCashPendingData(CashBackData cashBackData);

        void updateHeaderTokoPointData(TokoPointDrawerData tokoPointDrawerData);

        void getShopInfo(String url, String shopDomain);

        void openProductPageIfValid(String url, String shopDomain);

        void onDigitalItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);
    }
}
