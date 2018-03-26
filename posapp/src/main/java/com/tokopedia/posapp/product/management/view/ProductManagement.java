package com.tokopedia.posapp.product.management.view;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;

import java.util.List;

/**
 * @author okasurya on 3/14/18.
 */

public interface ProductManagement {
    interface Presenter extends CustomerPresenter<View> {
        void reload(String etalaseId);

        void loadMore(String etalaseId);
    }

    interface View extends CustomerView {
        void onReloadData(List<Visitable> list);

        void onLoadMore(List<Visitable> list);

        void onError(Throwable e);
    }
}
