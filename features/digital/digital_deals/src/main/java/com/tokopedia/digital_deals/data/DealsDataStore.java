package com.tokopedia.digital_deals.data;


import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse.DealDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;

import java.util.HashMap;

import rx.Observable;



public interface DealsDataStore {

    Observable<DealsResponse> getDeals(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchDeals(HashMap<String, Object> params);

    Observable<SearchResponse> getSearchNext(String nextUrl);

    Observable<BrandDetailsResponse> getBrandDetails(String url);

    Observable<DealDetailsResponse> getDealDetails(String url);

}
