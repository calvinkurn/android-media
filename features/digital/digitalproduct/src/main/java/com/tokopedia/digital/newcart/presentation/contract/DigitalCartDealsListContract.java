package com.tokopedia.digital.newcart.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.digital.newcart.domain.model.DealProductViewModel;

public interface DigitalCartDealsListContract {
    interface View extends BaseListViewListener<DealProductViewModel> {

        void setNextUrl(String nextUrl);

        void navigateToDetailPage(DealProductViewModel dealApplink);
    }

    interface Presenter extends CustomerPresenter<View> {

        void getProducts(String nextUrl);

        void onDealDetailClicked(DealProductViewModel productViewModel);
    }
}
