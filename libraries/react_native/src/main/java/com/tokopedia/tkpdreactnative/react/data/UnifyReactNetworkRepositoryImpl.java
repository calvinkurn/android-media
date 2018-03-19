package com.tokopedia.tkpdreactnative.react.data;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkAuthDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkBearerDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkDataSource;
import com.tokopedia.tkpdreactnative.react.data.datasource.UnifyReactNetworkWsV4AuthDataSource;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.tkpdreactnative.react.domain.UnifyReactNetworkRepository;

import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkRepositoryImpl implements UnifyReactNetworkRepository {
    private UnifyReactNetworkDataSource unifyReactNetworkDataSource;
    private UnifyReactNetworkAuthDataSource unifyReactNetworkAuthDataSource;
    private UnifyReactNetworkBearerDataSource unifyReactNetworkBearerDataSource;
    private UnifyReactNetworkWsV4AuthDataSource unifyReactNetworkWsV4AuthDataSource;

    public UnifyReactNetworkRepositoryImpl(UnifyReactNetworkDataSource unifyReactNetworkDataSource,
                                           UnifyReactNetworkAuthDataSource unifyReactNetworkAuthDataSource,
                                           UnifyReactNetworkBearerDataSource unifyReactNetworkBearerDataSource,
                                           UnifyReactNetworkWsV4AuthDataSource unifyReactNetworkWsV4AuthDataSource) {
        this.unifyReactNetworkDataSource = unifyReactNetworkDataSource;
        this.unifyReactNetworkAuthDataSource = unifyReactNetworkAuthDataSource;
        this.unifyReactNetworkBearerDataSource = unifyReactNetworkBearerDataSource;
        this.unifyReactNetworkWsV4AuthDataSource = unifyReactNetworkWsV4AuthDataSource;
    }

    @Override
    public Observable<String> request(ReactNetworkingConfiguration configuration) {
        switch (configuration.getAuthorizationMode()) {
            case ReactConst.Networking.WSAUTH:
                if (isWsV4Url(configuration.getUrl())) return unifyReactNetworkWsV4AuthDataSource.request(configuration);
                else return unifyReactNetworkAuthDataSource.request(configuration);
            case ReactConst.Networking.BEARER:
                return unifyReactNetworkBearerDataSource.request(configuration);
            default:
                return unifyReactNetworkDataSource.request(configuration);
        }
    }

    private Boolean isWsV4Url(String url) {
        return url.contains("ws.tokopedia.com");
    }
}
