package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.presentation.listener.BaseAccountView;

import java.util.List;


/**
 * @author okasurya on 7/17/18.
 */
public interface BuyerAccount {
    interface View extends BaseAccountView {
        void loadBuyerData(BuyerViewModel model);
        void hideLoadMoreLoading();
        void showLoadMoreLoading();
        void onRenderRecomAccountBuyer(List<Visitable> list);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getBuyerData(String query, String saldoQuery);
        void getFirstRecomData();
        void getRecomData(int page);
    }
}
