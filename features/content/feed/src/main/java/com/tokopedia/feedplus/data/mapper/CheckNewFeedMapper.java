package com.tokopedia.feedplus.data.mapper;

import com.tkpdfeed.feeds.FeedCheck;
import com.tokopedia.feedplus.domain.model.CheckFeedDomain;

import javax.inject.Inject;

import rx.functions.Func1;

/**
 * @author by nisie on 8/23/17.
 */

public class CheckNewFeedMapper implements Func1<FeedCheck.Data, CheckFeedDomain> {

    @Inject
    public CheckNewFeedMapper() {
    }

    @Override
    public CheckFeedDomain call(FeedCheck.Data data) {
        if (data != null
                && data.checkFeed() != null
                && data.checkFeed().data() != null)
            return new CheckFeedDomain(data.checkFeed().data());
        else
            return new CheckFeedDomain("0");
    }
}
