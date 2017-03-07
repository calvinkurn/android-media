package com.tokopedia.core.network.apiservices.ace.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author erry on 07/03/17.
 */

public interface UniverseApi {

    @GET(TkpdBaseURL.Ace.PATH_UNIVERSE_SEARCH)
    Observable<Response<String>> getUniverseSearch(@QueryMap Map<String, String> params);

    @DELETE(TkpdBaseURL.Ace.PATH_DELETE_SEARCH)
    Observable<Response<Void>> deleteRecentSearch(@QueryMap Map<String, String> params);
}
