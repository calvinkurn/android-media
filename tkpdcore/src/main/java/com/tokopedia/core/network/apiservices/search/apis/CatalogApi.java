package com.tokopedia.core.network.apiservices.search.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface CatalogApi {

    @GET(TkpdBaseURL.Search.PATH_GET_CATALOG_DETAIL)
    Observable<Response<TkpdResponse>> getCatalogDetail(@QueryMap TKPDMapParam<String, String> params);

}
