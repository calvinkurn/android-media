package com.tokopedia.feedplus.data.factory;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.feedplus.data.mapper.FeedResultMapper;
import com.tokopedia.feedplus.data.mapper.HomeFeedMapper;
import com.tokopedia.feedplus.data.source.cloud.HomeFeedDataSource;

/**
 * Created by henrypriyono on 12/29/17.
 */

public class HomeFeedFactory {

    private final ApolloClient apolloClient;
    private final HomeFeedMapper homeFeedMapper;
    private final FeedResultMapper feedResultMapper;

    public HomeFeedFactory(ApolloClient apolloClient,
                           HomeFeedMapper homeFeedMapper,
                           FeedResultMapper feedResultMapper) {

        this.apolloClient = apolloClient;
        this.homeFeedMapper = homeFeedMapper;
        this.feedResultMapper = feedResultMapper;
    }

    public HomeFeedDataSource createHomeFeedDataSource() {
        return new HomeFeedDataSource(apolloClient, homeFeedMapper, feedResultMapper);
    }
}
