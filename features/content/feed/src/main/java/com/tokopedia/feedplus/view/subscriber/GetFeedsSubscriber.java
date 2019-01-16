package com.tokopedia.feedplus.view.subscriber;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.feedplus.view.analytics.FeedAnalytics;
import com.tokopedia.feedplus.view.listener.FeedPlus;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;

import java.util.ArrayList;

/**
 * @author by nisie on 5/29/17.
 */

public class GetFeedsSubscriber extends GetFirstPageFeedsSubscriber {

    public GetFeedsSubscriber(FeedPlus.View viewListener, int page, FeedAnalytics analytics) {
        super(viewListener, page, analytics);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e.printStackTrace();
        }

        viewListener.unsetEndlessScroll();
        viewListener.onShowRetryGetFeed();
        viewListener.hideAdapterLoading();
    }

    @Override
    public void onNext(FeedResult feedResult) {
        ArrayList<Visitable> list = convertToViewModel(feedResult.getFeedDomain());

        viewListener.hideAdapterLoading();

        if (list.size() == 0) {
            viewListener.unsetEndlessScroll();
        } else {
            viewListener.onSuccessGetFeed(list);

            if (feedResult.isHasNext()) {
                viewListener.updateCursor(getCurrentCursor(feedResult));
            } else {
                viewListener.unsetEndlessScroll();
            }
        }
    }
}
