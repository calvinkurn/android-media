package com.tokopedia.home.beranda.presentation.view.subscriber;

import com.crashlytics.android.Crashlytics;
import com.tokopedia.home.BuildConfig;
import com.tokopedia.home.beranda.data.mapper.FeedTabMapper;
import com.tokopedia.home.beranda.presentation.view.HomeContract;
import com.tokopedia.home.beranda.presentation.view.viewmodel.FeedTabModel;
import com.tokopedia.kotlin.util.ContainNullException;
import com.tokopedia.kotlin.util.NullCheckerKt;

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
        NullCheckerKt.isContainNull(feedTabModelList, errorMessage -> {
            String message = String.format("Found %s in %s",
                    errorMessage, GetFeedTabsSubscriber.class.getSimpleName());
            ContainNullException exception = new ContainNullException(message);
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(exception);
            }
            throw exception;
        });
        viewListener.onTabFeedLoadSuccess(feedTabModelList);
    }
}
