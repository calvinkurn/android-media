package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel;

import rx.Subscriber;

/**
 * Created by henrypriyono on 1/3/18.
 */

public class GetHomeFeedsSubscriber extends Subscriber<HomeRecommendationDataModel> {

    private final HomeFeedContract.View viewListener;

    public GetHomeFeedsSubscriber(HomeFeedContract.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.showGetListError(e);
    }

    @Override
    public void onNext(HomeRecommendationDataModel model) {
//        viewListener.renderList(model.getHomeRecommendations(), model.isHasNextPage());
    }
}
