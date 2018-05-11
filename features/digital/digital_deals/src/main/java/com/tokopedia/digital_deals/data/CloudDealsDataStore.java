package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.dealdetailsresponse.DealDetailsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.data.source.DealsApi;

import java.util.HashMap;
import rx.Observable;

public class CloudDealsDataStore implements DealsDataStore {

    private final DealsApi dealsApi;

    public CloudDealsDataStore(DealsApi dealsApi){
        this.dealsApi =dealsApi;
    }

    @Override
    public Observable<DealsResponse> getDeals(HashMap<String, Object> params) {
        return dealsApi.getDeals();
    }

    @Override
    public Observable<SearchResponse> getSearchDeals(HashMap<String, Object> params) {
        return dealsApi.getSearchDeals(params);
    }

    @Override
    public Observable<SearchResponse> getSearchNext(String nextUrl) {
        return dealsApi.getSearchNext(nextUrl);
    }

    @Override
    public Observable<BrandDetailsResponse> getBrandDetails(String url) {
        return dealsApi.getBrandDetails(url);
    }

    @Override
    public Observable<DealDetailsResponse> getDealDetails(String url) {
        return dealsApi.getDealDetails(url);
    }


}
