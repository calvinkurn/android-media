package com.tokopedia.core.react.domain;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.react.UnknownMethodException;

import rx.Observable;

/**
 * @author ricoharisin .
 */

public interface ReactNetworkRepository {


    Observable<String> getResponse(String url, String method, TKPDMapParam<String, String> params, Boolean isAuth) throws UnknownMethodException;
}
