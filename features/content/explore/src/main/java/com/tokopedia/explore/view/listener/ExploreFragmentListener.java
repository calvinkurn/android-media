package com.tokopedia.explore.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by milhamj on 20/07/18.
 */

public interface ExploreFragmentListener {

    interface View extends CustomerView {

        void updateCursor(String cursor);

        void updateCategoryId(int categoryId);
    }

    interface Presenter extends CustomerPresenter<View> {
        void getExploreData();

        void updateCursor(String cursor);

        void updateCategoryId(int categoryId);
    }
}
