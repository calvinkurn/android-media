package com.tokopedia.core.network.apiservices.search.apis;

import com.tokopedia.core.catalog.model.CatalogDetailListData;
import com.tokopedia.core.network.constants.TkpdBaseURL;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Tkpd_Eka on 1/19/2016.
 */
public interface CatalogAWSApi {

    @GET(TkpdBaseURL.Search.URL_CATALOG_SELLER)
    Observable<Response<String>> getSellerList(
            @Query("ctg_id") String ctgId,
            @Query("start") int start,
            @Query("row") int row,
            @Query("ob") String orderBy,
            @Query("condition") String condition);

    @GET(TkpdBaseURL.Search.URL_CATALOG_SELLER)
    Observable<Response<String>> getSellerListLocation(
            @Query("ctg_id") String ctgId,
            @Query("start") int start,
            @Query("row") int row,
            @Query("ob") String orderBy,
            @Query("condition") String condition,
            @Query("floc") String floc);

    @GET(TkpdBaseURL.Search.URL_CATALOG_SELLER)
    Observable<Response<CatalogDetailListData>> getCatalogDetailList(
            @QueryMap Map<String, String> map);

}
