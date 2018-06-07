package com.tokopedia.digital_deals.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.LikeUpdateResponse;
import com.tokopedia.digital_deals.data.entity.response.allbrandsresponse.AllBrandsResponse;
import com.tokopedia.digital_deals.data.entity.response.alllocationresponse.LocationResponse;
import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.categorydetailresponse.CategoryResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface DealsApi {

    @GET(DealsUrl.DEALS_LIST + "/{url}")
    Observable<DealsResponse> getDeals(@Path("url") String url);

    @GET()
    Observable<DealsResponse> getDealsNextPage(@Url String nextUrl);

    @GET(DealsUrl.DEALS_LIST_SEARCH)
    Observable<SearchResponse> getSearchDeals(@QueryMap Map<String, Object> param);

    @GET()
    Observable<SearchResponse> getSearchNext(@Url String nextUrl);

//    @GET()
//    Observable<BrandDetailsResponse> getBrandDetails(@Url String brandDetailUrl);

    @GET()
    Observable<BrandDetailsResponse> getBrandDetails(@Url String brandDetailUrl);

    @GET(DealsUrl.DEALS_PRODUCT + "/{url}")
    Observable<JsonObject> getDealDetails(@Path("url") String dealDetailUrl);

    @GET(DealsUrl.DEALS_LOCATIONS)
    Observable<LocationResponse> getLocations();

    @GET()
    Observable<CategoryResponse> getCategoryDetails(@Url String url);

    @GET()
    Observable<CategoryResponse> getCategoryDetailsNext(@Url String nextUrl);


    @GET(DealsUrl.DEALS_LIST_SEARCH)
    Observable<AllBrandsResponse> getAllBrandsByCategory(@QueryMap Map<String, Object> param);

    @GET()
    Observable<AllBrandsResponse> getAllBrandsNext(@Url String nextUrl);

    @POST(DealsUrl.DEALS_LIKES)
    @Headers({"Content-Type: application/json"})
    Observable<LikeUpdateResponse> updateLikes(@Body JsonObject requestBody);
}
