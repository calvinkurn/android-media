package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.home.BuildConfig;
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper;
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedListModel;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;

import rx.Subscriber;

/**
 * Created by henrypriyono on 1/3/18.
 */

public class GetHomeFeedsSubscriber extends Subscriber<HomeFeedListModel> {

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
    public void onNext(HomeFeedListModel model) {
        viewListener.renderList(model.getHomeFeedViewModels(), model.isHasNextPage());
    }
}
