package com.tokopedia.core.network.apiservices.search.apis;

import com.tokopedia.core.myproduct.model.CatalogDataModel;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */
public interface CatalogApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Search.PATH_GET_CATALOG)
    Observable<Response<TkpdResponse>> getCatalog(@FieldMap Map<String, String> params);

    @GET(TkpdBaseURL.Search.PATH_GET_CATALOG)
    Observable<CatalogDataModel> getCatalog2(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.Search.URL_CATALOG + TkpdBaseURL.Search.PATH_GET_CATALOG)
    Observable<CatalogDataModel> getCatalog(
            @Header(NetworkCalculator.CONTENT_MD5) String contentMD5,// 1
            @Header(NetworkCalculator.DATE) String date,// 2
            @Header(NetworkCalculator.AUTHORIZATION) String authorization, // 3
            @Header(NetworkCalculator.X_METHOD) String xMethod,// 4
            @Query(NetworkCalculator.USER_ID) String userId,// 5
            @Query(NetworkCalculator.DEVICE_ID) String deviceId, // 6
            @Query(NetworkCalculator.HASH) String hash,// 7
            @Query(NetworkCalculator.DEVICE_TIME) String deviceTime,// 8
            @Query("product_department_id") String productDepartmentId,
            @Query("product_name") String productName
    );

    @GET(TkpdBaseURL.Search.PATH_GET_CATALOG_DETAIL)
    Observable<Response<TkpdResponse>> getCatalogDetail(@QueryMap TKPDMapParam<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Search.PATH_GET_SELL_FORM)
    Observable<Response<TkpdResponse>> getSellForm(@FieldMap Map<String, String> params);
}
