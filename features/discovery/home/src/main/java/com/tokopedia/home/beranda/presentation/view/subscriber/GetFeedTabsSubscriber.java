package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by henrypriyono on 1/3/18.
 */

public class GetFeedTabsSubscriber extends Subscriber<List<FeedTabModel>> {

    private final HomeContract.View viewListener;

    public GetFeedTabsSubscriber(HomeContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.onTabFeedLoadError(e);
    }

    @Override
    public void onNext(List<FeedTabModel> feedTabModelList) {
        viewListener.onTabFeedLoadSuccess(feedTabModelList);
    }
}
