package com.tokopedia.feedplus.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FeedCheck;
import com.tokopedia.feedplus.data.mapper.CheckNewFeedMapper;
import com.tokopedia.feedplus.domain.model.CheckFeedDomain;
import com.tokopedia.feedplus.domain.usecase.CheckNewFeedUseCase;
import com.tokopedia.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * @author by nisie on 8/23/17.
 */

public class CloudCheckNewFeedDataSource {

    private final ApolloClient apolloClient;
    private final CheckNewFeedMapper checkNewFeedMapper;

    public CloudCheckNewFeedDataSource(ApolloClient apolloClient,
                                       CheckNewFeedMapper checkNewFeedMapper) {
        this.apolloClient = apolloClient;
        this.checkNewFeedMapper = checkNewFeedMapper;

    }

    public Observable<CheckFeedDomain> checkNewFeed(RequestParams requestParams) {
        ApolloWatcher<FeedCheck.Data> apolloWatcher = apolloClient.newCall(FeedCheck.builder()
                .userID(requestParams.getInt(GetFeedsUseCase.PARAM_USER_ID, 0))
                .cursor(requestParams.getString(CheckNewFeedUseCase.PARAM_CURSOR, ""))
                .build()).watcher();

        return RxApollo.from(apolloWatcher).map(checkNewFeedMapper);
    }
}
