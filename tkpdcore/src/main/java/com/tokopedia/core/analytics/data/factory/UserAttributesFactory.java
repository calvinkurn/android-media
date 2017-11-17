package com.tokopedia.core.analytics.data.factory;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.analytics.data.source.CloudAttrDataSource;

/**
 * Created by Herdi_WORK on 03.10.17.
 */

public class UserAttributesFactory {

    private ApolloClient apolloClient;

    public UserAttributesFactory(ApolloClient aplClient){
        apolloClient = aplClient;
    }

    public CloudAttrDataSource createCloudAttrDataSource(){
        return new CloudAttrDataSource(apolloClient);
    }


}
