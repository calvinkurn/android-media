package com.tokopedia.tkpdreactnative.react.data;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.UnknownMethodException;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkDefaultAuthFactory;
import com.tokopedia.tkpdreactnative.react.data.factory.ReactNetworkFactory;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkRepository;
import com.tokopedia.user.session.UserSessionInterface;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public class ReactNetworkRepositoryImpl implements ReactNetworkRepository {

    private ReactNetworkAuthFactory reactNetworkAuthFactory;
    private ReactNetworkFactory reactNetworkFactory;
    private ReactNetworkDefaultAuthFactory reactNetworkDefaultAuthFactory;
    private UserSessionInterface userSession;

    public ReactNetworkRepositoryImpl(ReactNetworkAuthFactory reactNetworkAuthFactory,
                                      ReactNetworkFactory reactNetworkFactory,
                                      ReactNetworkDefaultAuthFactory reactNetworkDefaultAuthFactory,
                                      UserSessionInterface userSession) {
        this.reactNetworkAuthFactory = reactNetworkAuthFactory;
        this.reactNetworkFactory = reactNetworkFactory;
        this.reactNetworkDefaultAuthFactory = reactNetworkDefaultAuthFactory;
        this.userSession = userSession;
    }

    @Override
    public Observable<String> getResponse(String url, String method, TKPDMapParam<String, String> params, Boolean isAuth) throws UnknownMethodException {
        switch (method) {
            case ReactConst.GET:
                if (isAuth) {
                    if (isWSV4(url)) {
                        return reactNetworkDefaultAuthFactory.createReactNetworkDataSource().get(url,
                                AuthHelper.generateParamsNetwork(
                                        userSession.getUserId(),
                                        userSession.getDeviceId(),
                                        params));
                    } else {
                        return reactNetworkAuthFactory.createReactNetworkDataSource().get(url, params);
                    }
                } else return reactNetworkFactory.createReactNetworkDataSource().get(url, params);
            case ReactConst.POST:
                if (isAuth) {
                    if (isWSV4(url)) {
                        return reactNetworkDefaultAuthFactory.createReactNetworkDataSource().post(url,
                                AuthHelper.generateParamsNetwork(
                                        userSession.getUserId(),
                                        userSession.getDeviceId(),
                                        params));
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

    @Override
    public Observable<String> getResponseJson(String url, String method, String params, Boolean isAuth) throws UnknownMethodException {
        if (ReactConst.POST.equals(method)) {
            if (isAuth) {
                return reactNetworkAuthFactory.createReactNetworkDataSource().postJson(url, params);
            } else return reactNetworkFactory.createReactNetworkDataSource().postJson(url, params);
        }
        throw new UnknownMethodException();
    }

    @Override
    public Observable<String> getResponseParam(String url, String method, String params, Boolean isAuth) throws UnknownMethodException {
        switch(method) {
            case ReactConst.GET:
                if (isAuth) {
                    return reactNetworkAuthFactory.createReactNetworkDataSource().getParam(url, params);
                }
                else return reactNetworkFactory.createReactNetworkDataSource().getParam(url, params);
            case ReactConst.POST:
                if (isAuth) {
                    return reactNetworkAuthFactory.createReactNetworkDataSource().postParam(url, params);
                }
                else return reactNetworkFactory.createReactNetworkDataSource().postParam(url, params);
            default:
                throw new UnknownMethodException();
        }
    }


    private Boolean isWSV4(String url) {
        return url.contains("ws.tokopedia.com");
    }

}
