package com.tokopedia.core.network.apiservices.search.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface SearchApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Search.PATH_SEARCH_CATALOG)
    Observable<Response<TkpdResponse>> searchCatalog(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Search.PATH_SEARCH_PRODUCT)
    Observable<Response<TkpdResponse>> searchProduct(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Search.PATH_SEARCH_SHOP)
    Observable<Response<TkpdResponse>> searchShop(@FieldMap Map<String, String> params);
}
