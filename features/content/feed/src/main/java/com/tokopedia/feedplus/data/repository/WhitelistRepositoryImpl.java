package com.tokopedia.feedplus.data.repository;

import com.tokopedia.feedplus.data.factory.FeedFactory;
import com.tokopedia.feedplus.domain.model.feed.WhitelistDomain;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by yfsx on 21/06/18.
 */
public class WhitelistRepositoryImpl implements WhitelistRepository {

    public final FeedFactory feedFactory;

    public WhitelistRepositoryImpl(FeedFactory feedFactory) {
        this.feedFactory = feedFactory;
    }

    @Override
    public Observable<WhitelistDomain> getWhitelist(RequestParams requestParams) {
        return feedFactory.createWhitelistDataSource().getWhiteList(requestParams);
    }
}
