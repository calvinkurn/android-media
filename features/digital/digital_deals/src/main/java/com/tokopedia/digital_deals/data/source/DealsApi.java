package com.tokopedia.digital_deals.data.source;

import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface DealsApi {

    @GET(DealsUrl.DEALS_LIST)
    Observable<DealsResponseEntity> getDeals();

    @GET(DealsUrl.DEALS_LIST_SEARCH)
    Observable<SearchResponse> getSearchDeals(@QueryMap Map<String, Object> param);

    @GET()
    Observable<SearchResponse> getSearchNext(@Url String nextUrl);

}
