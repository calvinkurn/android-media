package com.tokopedia.home.account.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

/**
 * @author okasurya on 7/17/18.
 */
public interface BuyerAccount {
    interface View extends CustomerView {
        void loadData(List<Visitable> visitables);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getData();
    }
}
