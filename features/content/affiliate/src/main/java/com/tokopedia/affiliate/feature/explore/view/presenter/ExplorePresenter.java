package com.tokopedia.affiliate.feature.explore.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.affiliate.feature.explore.view.listener.ExploreContract;

import javax.inject.Inject;

/**
 * @author by yfsx on 24/09/18.
 */
public class ExplorePresenter extends BaseDaggerPresenter<ExploreContract.View> implements ExploreContract.Presenter {

    @Inject
    public ExplorePresenter() {

    }

    @Override
    public void getFirstData(String searchKey, boolean isPullToRefresh) {


    }

    @Override
    public void loadMoreData(String cursor, String searchKey) {

    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
