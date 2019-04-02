package com.tokopedia.posapp.product.management.view;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.posapp.base.domain.model.DataStatus;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

/**
 * @author okasurya on 3/14/18.
 */

public interface ProductManagement {
    interface Presenter extends CustomerPresenter<View> {
        void reload();

        void loadMore();

        void editStatus(ProductViewModel productViewModel, boolean status, int position);
    }

    interface View extends CustomerView {
        void showLoadingDialog();

        void hideLoadingDialog();

        void onReloadData(List<Visitable> list);

        void onLoadMore(List<Visitable> list);

        void onErrorLoadData(Throwable e);

        void onSuccessEditStatus(int position, ProductViewModel productViewModel);

        void onErorEditStatus(int position);
    }
}
