package com.tokopedia.analyticsdebugger.debugger.ui.presenter;

import android.content.Context;
import android.net.Uri;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

import java.util.List;

public interface FpmDebugger {
    interface View extends CustomerView {
        void onLoadMoreCompleted(List<Visitable> visitables);

        void onReloadCompleted(List<Visitable> visitables);

        void onDeleteCompleted();

        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {
        void loadMore();

        void search(String text);

        void reloadData();

        void deleteAll();

        void writeAllDataToFile(Uri fileUri);
    }
}
