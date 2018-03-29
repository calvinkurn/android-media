package com.tokopedia.tkpdreactnative.react.data;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.UnknownMethodException;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkDefaultAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkFactory;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class ReactNetworkRepositoryImpl implements ReactNetworkRepository {

    private ReactNetworkAuthFactory reactNetworkAuthFactory;
    private ReactNetworkFactory reactNetworkFactory;
    private ReactNetworkDefaultAuthFactory reactNetworkDefaultAuthFactory;
    private Context context;

    public ReactNetworkRepositoryImpl(Context context, ReactNetworkAuthFactory reactNetworkAuthFactory,
                                      ReactNetworkFactory reactNetworkFactory,
                                      ReactNetworkDefaultAuthFactory reactNetworkDefaultAuthFactory) {
        this.reactNetworkAuthFactory = reactNetworkAuthFactory;
        this.reactNetworkFactory = reactNetworkFactory;
        this.reactNetworkDefaultAuthFactory = reactNetworkDefaultAuthFactory;
        this.context = context;
    }

    @Override
    public Observable<String> getResponse(String url, String method, TKPDMapParam<String, String> params, Boolean isAuth) throws UnknownMethodException {
        switch (method) {
            case ReactConst.GET:
                if (isAuth) {
                    if (isWSV4(url)) {
                        return reactNetworkDefaultAuthFactory.createReactNetworkDataSource().get(url, AuthUtil.generateParamsNetwork(context, params));
                    } else {
                        return reactNetworkAuthFactory.createReactNetworkDataSource().get(url, params);
                    }
                } else return reactNetworkFactory.createReactNetworkDataSource().get(url, params);
            case ReactConst.POST:
                if (isAuth) {
                    if (isWSV4(url)) {
                        return reactNetworkDefaultAuthFactory.createReactNetworkDataSource().post(url, AuthUtil.generateParamsNetwork(context, params));
                    } else {
                        return reactNetworkAuthFactory.createReactNetworkDataSource().post(url, params);
                    }
                } else return reactNetworkFactory.createReactNetworkDataSource().post(url, params);
            case ReactConst.DELETE:
                if (isAuth) {
                    return reactNetworkAuthFactory.createReactNetworkDataSource().delete(url);
                } else {
                    return reactNetworkFactory.createReactNetworkDataSource().delete(url);
                }
            default:
                throw new UnknownMethodException();
        }
    }

    private Boolean isWSV4(String url) {
        return url.contains("ws.tokopedia.com");
    }

}
