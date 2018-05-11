package com.tokopedia.digital_deals.data.source;

import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse.DealDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface DealsApi {

    @GET(DealsUrl.DEALS_LIST)
    Observable<DealsResponse> getDeals();

    @GET(DealsUrl.DEALS_LIST_SEARCH)
    Observable<SearchResponse> getSearchDeals(@QueryMap Map<String, Object> param);

    @GET()
    Observable<SearchResponse> getSearchNext(@Url String nextUrl);

    @GET()
    Observable<BrandDetailsResponse> getBrandDetails(@Url String brandDetailUrl);

    @GET()
    Observable<DealDetailsResponse> getDealDetails(@Url String dealDetailUrl);

}
