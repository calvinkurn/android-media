package com.tokopedia.explore.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.explore.view.listener.ExploreFragmentListener;

/**
 * @author by milhamj on 23/07/18.
 */

public class ContentExplorePresenter
        extends BaseDaggerPresenter<ExploreFragmentListener.View>
        implements ExploreFragmentListener.Presenter {

    String cursor = "";
    int categoryId = 0;

    @Override
    public void getExploreData() {

    }

    @Override
    public void updateCursor(String cursor) {

    }

    @Override
    public void updateCategoryId(int categoryId) {

    }
}
