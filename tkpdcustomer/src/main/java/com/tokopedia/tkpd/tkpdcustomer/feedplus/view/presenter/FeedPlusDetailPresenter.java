package com.tokopedia.tkpd.tkpdcustomer.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.tkpd.tkpdcustomer.feedplus.view.FeedPlusDetail;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter  {

    @Inject
    FeedPlusDetailPresenter() {

    }

    @Override
    public void attachView(FeedPlusDetail.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
