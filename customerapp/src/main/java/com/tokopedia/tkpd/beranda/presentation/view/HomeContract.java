package com.tokopedia.tkpd.beranda.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.tkpd.beranda.domain.model.category.CategoryLayoutRowModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;

import java.util.List;

/**
 * @author by errysuprayogi on 11/28/17.
 */

public interface HomeContract {

    interface View extends CustomerView {
        void showLoading();

        void hideLoading();

        void addItems(List<Visitable> items);

        void showNetworkError();

        void removeNetworkError();

        String getString(@StringRes int res);

        Context getContext();

        void openWebViewURL(String url, Context context);

        Activity getActivity();

        void onGimickItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);

        void setSaldoItem(SaldoViewModel cashViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getHomeData();

        void getShopInfo(String url, String shopDomain);

        void openProductPageIfValid(String url, String shopDomain);

        void onDigitalItemClicked(CategoryLayoutRowModel data, int parentPosition, int childPosition);
    }
}
