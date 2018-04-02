package com.tokopedia.posapp.product.management.view;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.posapp.product.management.view.viewmodel.ProductViewModel;
import com.tokopedia.posapp.shop.domain.model.EtalaseDomain;

import java.util.List;

/**
 * @author okasurya on 3/14/18.
 */

public interface ProductManagement {
    interface Presenter extends CustomerPresenter<View> {
        void reload();

        void load(String etalaseId);

        void loadMore(String etalaseId);
    }

    interface View extends CustomerView {
        void onGetEtalaseCompleted(List<EtalaseDomain> etalaseDomains);

        void onReloadData(List<Visitable> list);

        void onLoadMore(List<Visitable> list);

        void onError(Throwable e);
    }
}
