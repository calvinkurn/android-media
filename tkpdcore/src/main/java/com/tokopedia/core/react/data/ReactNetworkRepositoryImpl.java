package com.tokopedia.core.react.data;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.react.UnknownMethodException;
import com.tokopedia.core.react.data.factory.ReactNetworkAuthFactory;
import com.tokopedia.core.react.data.factory.ReactNetworkFactory;
import com.tokopedia.core.react.domain.ReactNetworkRepository;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class ReactNetworkRepositoryImpl implements ReactNetworkRepository {

    private static final String GET = "GET";
    private static final String POST = "POST";
    private ReactNetworkAuthFactory reactNetworkAuthFactory;
    private ReactNetworkFactory reactNetworkFactory;

    public ReactNetworkRepositoryImpl(ReactNetworkAuthFactory reactNetworkAuthFactory, ReactNetworkFactory reactNetworkFactory) {
        this.reactNetworkAuthFactory = reactNetworkAuthFactory;
        this.reactNetworkFactory = reactNetworkFactory;
    }

    @Override
    public Observable<String> getResponse(String url, String method, TKPDMapParam<String, Object> params, Boolean isAuth) throws UnknownMethodException {
        switch(method) {
            case GET:
                if (isAuth) return reactNetworkAuthFactory.createReactNetworkDataSource().get(url, params);
                else return reactNetworkFactory.createReactNetworkDataSource().get(url, params);
            case POST:
                if (isAuth) return reactNetworkAuthFactory.createReactNetworkDataSource().post(url, params);
                else return reactNetworkFactory.createReactNetworkDataSource().post(url, params);
            default:
                throw new UnknownMethodException();
        }
    }

}
