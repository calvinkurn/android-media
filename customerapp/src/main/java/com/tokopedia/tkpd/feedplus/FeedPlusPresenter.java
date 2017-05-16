package com.tokopedia.tkpd.feedplus;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

import javax.inject.Inject;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusPresenter
        extends BaseDaggerPresenter<FeedPlus.View>
        implements FeedPlus.Presenter {

    @Inject
    FeedPlusPresenter() {

    }

    @Override
    public void attachView(FeedPlus.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


}
