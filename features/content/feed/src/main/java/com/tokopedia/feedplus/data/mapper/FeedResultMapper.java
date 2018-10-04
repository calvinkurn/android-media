package com.tokopedia.feedplus.data.mapper;

import com.tokopedia.feedplus.domain.model.feed.FeedDomain;
import com.tokopedia.feedplus.domain.model.feed.FeedResult;

import rx.functions.Func1;

/**
 * @author ricoharisin .
 */

public class FeedResultMapper implements Func1<FeedDomain, FeedResult> {

    private int dataSource;

    public FeedResultMapper(int dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public FeedResult call(FeedDomain feedDomain) {
        return new FeedResult(feedDomain, dataSource, feedDomain.isHasNext());
    }
}
