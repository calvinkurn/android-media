package com.tokopedia.core.network.apiservices.search.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public interface HotListApi {

    @GET(TkpdBaseURL.Search.PATH_GET_HOTLIST)
    Observable<Response<TkpdResponse>> getHotList(@QueryMap Map<String, String> params);
}
