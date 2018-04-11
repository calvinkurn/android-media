package com.tokopedia.digital_deals.data;

import com.tokopedia.digital_deals.data.entity.response.DealsResponseEntity;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.data.source.DealsApi;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.HashMap;
import rx.Observable;

public class CloudDealsDataStore implements DealsDataStore {

    private final DealsApi dealsApi;

    public CloudDealsDataStore(DealsApi dealsApi){
        this.dealsApi =dealsApi;
    }

    @Override
    public Observable<DealsResponseEntity> getDeals(HashMap<String, Object> params) {
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
}
