package com.tokopedia.core.drawer2.data.factory;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.source.CloudDepositSource;
import com.tokopedia.core.network.apiservices.transaction.DepositService;

/**
 * Created by nisie on 5/4/17.
 */

public class DepositSourceFactory {

    private final Context context;
    private ApolloClient apolloClient;
    private DepositMapper depositMapper;
    private LocalCacheHandler drawerCache;

    public DepositSourceFactory(Context context,
                                ApolloClient apolloClient,
                                DepositMapper depositMapper,
                                LocalCacheHandler drawerCache) {
        this.context = context;
        this.apolloClient = apolloClient;
        this.depositMapper = depositMapper;
        this.drawerCache = drawerCache;
    }

    public CloudDepositSource createCloudDepositSource() {
        return new CloudDepositSource(context, apolloClient,
                depositMapper, drawerCache);
    }
}
