package com.tokopedia.analytics.debugger.ui;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsDebugger {
    interface View extends CustomerView {
        void onLoadMoreCompleted(List<Visitable> visitables);

        void onReloadCompleted(List<Visitable> visitables);

        void onDeleteCompleted();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadMore();

        void search(String text);

        void reloadData();

        void deleteAll();
    }
}
