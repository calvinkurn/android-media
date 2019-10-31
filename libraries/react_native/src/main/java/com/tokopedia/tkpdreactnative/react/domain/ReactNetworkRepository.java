package com.tokopedia.tkpdreactnative.react.domain;


import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.tkpdreactnative.react.UnknownMethodException;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface ReactNetworkRepository {


    Observable<String> getResponse(String url, String method, TKPDMapParam<String, String> params, Boolean isAuth) throws UnknownMethodException;

    Observable<String> getResponseJson(String url, String method, String body, Boolean isAuth) throws UnknownMethodException;

    Observable<String> getResponseParam(String url, String method, String body, Boolean isAuth) throws UnknownMethodException;
}
