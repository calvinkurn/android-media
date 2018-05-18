package com.tokopedia.feedplus.data.source;

import com.apollographql.android.rx.RxApollo;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.ApolloWatcher;
import com.tkpdfeed.feeds.FollowKol;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.feedplus.data.mapper.FollowKolMapper;
import com.tokopedia.feedplus.domain.model.FollowKolDomain;
import com.tokopedia.feedplus.domain.usecase.FollowKolPostUseCase;

import rx.Observable;

/**
 * @author by nisie on 11/3/17.
 */

public class KolSource {

    private final FollowKolMapper followKolMapper;
    private ApolloClient apolloClient;

    public KolSource(ApolloClient apolloClient,
                     FollowKolMapper followKolMapper) {
        this.apolloClient = apolloClient;
        this.followKolMapper = followKolMapper;
    }

    public Observable<FollowKolDomain> followKolPost(RequestParams requestParams) {
        ApolloWatcher<FollowKol.Data> apolloWatcher = apolloClient.newCall(
                FollowKol.builder()
                        .userID(requestParams.getInt(FollowKolPostUseCase.PARAM_USER_ID, 0))
                        .action(requestParams.getInt(FollowKolPostUseCase.PARAM_ACTION, -1))
                        .build()).watcher();

        return RxApollo.from(apolloWatcher).map(followKolMapper);
    }

}
